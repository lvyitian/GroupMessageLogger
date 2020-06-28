
package top.dsbbs2.gml;

import java.awt.EventQueue;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.internal.EventInternalJvmKt;
import net.mamoe.mirai.message.GroupMessageEvent;

public class LogWin
{

  private JFrame frame;
  private JEditorPane editorPane;

  /**
   * Launch the application.
   */
  public static void main(final String[] args)
  {
    EventQueue.invokeLater(() ->
    {
      try {
        final LogWin window = new LogWin();
        window.frame.setVisible(true);
      } catch (final Exception e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Create the application.
   * @wbp.parser.entryPoint
   */
  public LogWin()
  {
    this.initialize();
    EventInternalJvmKt._subscribeEventForJaptOnly(GroupMessageEvent.class, Global.INSTANCE.bot, e ->
    {
      if (Global.INSTANCE.config.getConfig().gp.contains(e.getGroup().getId())) {
        final String text = String.format("%s%n",LocalDateTime.now()
            .format(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd hh:mm:ss")
                .toFormatter(Locale.SIMPLIFIED_CHINESE))
            + " [" + e.getGroup().getId() + "]" + e.getSender().getId() + "(" + e.getSenderName() + "): "
            + e.getMessage().contentToString());
        LogWin.this.editorPane.setText(String.format("%s%s", LogWin.this.editorPane.getText(), text));
        if (LogWin.this.editorPane.getText().length()>3000) {
          LogWin.this.editorPane.setText("");
        }
        try {
          Global.INSTANCE.fout.write(text.getBytes(StandardCharsets.UTF_8));
          Global.INSTANCE.fout.flush();
        } catch (final Throwable e2) {
          Global.INSTANCE.ct(e2);
        }
      }
      return ListeningStatus.LISTENING;
    });
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize()
  {
    this.frame = new JFrame();
    frame.setResizable(false);
    this.frame.setBounds(100, 100, 405, 258);
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.getContentPane().setLayout(null);

    this.editorPane = new JEditorPane();
    this.editorPane.setEditable(false);
    this.editorPane.setBounds(0, 0, 389, 219);
    this.frame.getContentPane().add(this.editorPane);
  }
}
