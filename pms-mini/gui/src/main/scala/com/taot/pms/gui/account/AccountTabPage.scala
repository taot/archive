package com.taot.pms.gui.account

import java.awt.Color
import java.lang.{Long => JLong}

import scala.actors.{Actor, TIMEOUT}
import scala.actors.Actor._
import scala.swing._
import scala.swing.TabbedPane.Page
import scala.swing.event.ButtonClicked

import com.taot.pms.common.RichDomainObjectFactory
import com.taot.pms.common.logging.Logging
import javax.swing.table.DefaultTableModel
import org.joda.time.LocalDate
import com.taot.pms.rpc.thrift.{TAccountCreate, TAccount, PMSService}
import com.taot.pms.rpc.TypeConversions

object AccountTabPage extends Logging {

  val TABLE_COLUMN_NAMES = Array("Id", "Name", "Open Date")

  val thriftClient = RichDomainObjectFactory.getInstance(classOf[PMSService.FinagledClient])

  def apply(): Page = new Page("Account", new BoxPanel(Orientation.Vertical) {

    val createButton = new Button("Create")
    val updateButton = new Button("Update")
    val deleteButton = new Button("Delete")
    val refreshButton = new Button("Refresh")

    val buttonPanel = new FlowPanel(FlowPanel.Alignment.Leading)(
      createButton,
      updateButton,
      deleteButton,
      refreshButton
    )

    val accountTable = new Table(){
      showGrid = true
      gridColor = Color.BLACK
      model = new DefaultTableModel(0, TABLE_COLUMN_NAMES.size) {
        override def getColumnName(column: Int) = TABLE_COLUMN_NAMES(column)
        override def setValueAt(value: Any, row: Int, col: Int) {
          super.setValueAt(value, row, col)
        }
        override def isCellEditable(row: Int, column: Int) = false
      }
    }

    contents += buttonPanel
    contents += new ScrollPane(accountTable)

    /* UI Setup End */

    listenTo(createButton, updateButton, deleteButton, refreshButton)

    reactions += {
      case ButtonClicked(button) =>
        if (button == refreshButton) {
          triggerUpdateTable()
        } else if (button == createButton) {
          popupCreateAccountDialog()
        }
    }

    triggerUpdateTable()


    /*
     * Method definitions
     */

    private def popupCreateAccountDialog(): Unit = {
      val dialog = new AccountCreateDialog(this)
      dialog.onConfirm { a: TAccountCreate =>
        thriftClient.createAccount(a).onSuccess { id =>
          logger.info("Account {} created", id)
        }.onFailure { e =>
          throw e
        }
        triggerUpdateTable()
      }
      dialog.visible = true
    }

    private def triggerUpdateTable(): Unit = {
      refreshButton.enabled = false
      val actor = refreshActor()
      actor.start()
      loadAccounts(actor)
    }

    private def refreshActor() = new Actor {
      def act = {
        loop {
          reactWithin(10 * 1000) {
            case accounts: Seq[TAccount] =>
              updateAccountTable(accounts)
              refreshButton.enabled = true
            case TIMEOUT =>
              refreshButton.enabled = true
          }
        }
      }
    }

    private def updateAccountTable(accounts: Seq[TAccount]): Unit = {
      val model = accountTable.model.asInstanceOf[DefaultTableModel]
      // clear table
      val count = model.getRowCount
      for (i <- 0 until count) {
        model.removeRow(count - i - 1)
      }
      // add accounts
      for (acct <- accounts) {
        val openDate = TypeConversions.localDateFromThrift(acct.openDate)
        model.addRow(Array(JLong.valueOf(acct.id).asInstanceOf[AnyRef], acct.name, openDate))
      }
    }
  })

  private def loadAccounts(refreshActor: Actor): Unit = {
    actor {
      thriftClient.loadAllAccounts().onSuccess { accounts =>
        refreshActor ! accounts
      }
    }
  }
}
