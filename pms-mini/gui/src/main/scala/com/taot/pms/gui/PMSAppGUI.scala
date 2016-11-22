package com.taot.pms.gui

import scala.swing._
import java.awt
import com.taot.pms.gui.account.AccountTabPage
import org.springframework.scala.context.function.FunctionalConfigApplicationContext

object PMSAppGUI extends SimpleSwingApplication {

  val context = FunctionalConfigApplicationContext(classOf[GUIConfiguration])

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = {
      context.close()
    }
  })

  def top = new MainFrame {

    title = "PMS mini GUI"

    preferredSize = new Dimension(800, 600)

    contents = new TabbedPane {
      pages += AccountTabPage()
    }

    // TODO report a bug on this
    setLocationRelativeTo(new UIElement {
      def peer: awt.Component = null
    })
  }
}
