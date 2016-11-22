package com.taot.pms.gui.account

import scala.swing._
import scala.swing.event.ButtonClicked

import com.taot.pms.common.logging.Logging
import com.taot.pms.common.util.DateTimeUtil
import com.taot.pms.gui.NoLayoutPanel
import com.taot.pms.gui.exception.{InputException, InvalidInputException, MissingInputException}
import com.taot.pms.rpc.TypeConversions._
import com.taot.pms.rpc.thrift.{TAccount, TAccountCreate}
import net.java.dev.designgridlayout.DesignGridLayout

class AccountCreateDialog(parent: UIElement) extends Dialog with Logging {

  title = "Create Account"
  modal = true

  private val accountNameTextField = new TextField
  private val accountOpenDateTextField = new TextField
  private val accountInitialCashTextField = new TextField
  private val okButton = new Button("OK")
  private val cancelButton = new Button("Cancel")

  private val panel = new NoLayoutPanel();

  private val layout = new DesignGridLayout(panel.peer)
  layout.row().grid(new Label("Account Name:").peer).add(accountNameTextField.peer)
  layout.row().grid(new Label("Open Date:").peer).add(accountOpenDateTextField.peer)
  layout.row().grid(new Label("Initial Cash:").peer).add(accountInitialCashTextField.peer)
  layout.emptyRow()
  layout.row().right().add(okButton.peer).add(cancelButton.peer)


  // TODO The callback mechanism is not quite good and needs to refactor
  private var cancelCallback: ( Unit => Unit) = { Unit => }

  private var confirmCallback: (TAccountCreate => Unit) = { a => }

  contents = panel

  minimumSize = new Dimension(400, 150)

  setLocationRelativeTo(parent)

  /* UI Setup End */

  listenTo(okButton, cancelButton)

  reactions += {
    case ButtonClicked(button) =>
      if (button == okButton) {
        try {
          val accountCreate = getInputAccountCreate()
          if (confirmCallback != null) {
            confirmCallback(accountCreate)
          }
          dispose()
        } catch {
          case e: InputException =>
            Dialog.showMessage(null, e.getMessage, "Error")
          case e: Throwable =>
            logger.error(e.getMessage, e)
            Dialog.showMessage(null, e.getMessage, "Error")
        }

      } else if (button == cancelButton) {
        if (confirmCallback != null) {
          cancelCallback()
        }
        dispose()
      }
  }

  private def getInputAccountCreate(): TAccountCreate = {
    val name = accountNameTextField.text
    if (name == null || name.isEmpty) {
      throw new MissingInputException("account name")
    }
    val sOpenDate = accountOpenDateTextField.text
    val openDate = try {
      DateTimeUtil.parseLocalDate(sOpenDate)
    } catch {
      case e: Throwable =>
        throw new InvalidInputException("open date", sOpenDate)
    }
    val account = TAccount(-1, name, openDate)

    val sInitialCash = accountInitialCashTextField.text
    val initialCash = Integer.parseInt(sInitialCash)
    val initialMargin = 0.0

    TAccountCreate(account, initialCash)
  }

  def onConfirm(callback: TAccountCreate => Unit): Unit = confirmCallback = callback

  def onCancel(callback: Unit => Unit): Unit = cancelCallback = callback
}