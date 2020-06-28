
package top.dsbbs2.gml;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.SystemDeviceInfoKt;

public class Main
{

  private JFrame frame;
  private JTextField textField;
  private JPasswordField passwordField;
  private JTextField textField_1;

  /**
   * Launch the application.
   */
  public static void main(final String[] args)
  {
    EventQueue.invokeLater(() ->
    {
      try {
        final Main window = new Main();
        window.frame.setVisible(true);
      } catch (final Throwable e) {
        Global.INSTANCE.ct(e);
      }
    });
  }

  /**
   * Create the application.
   *
   * @wbp.parser.entryPoint
   */
  public Main()
  {
    Global.INSTANCE.main = this;
    this.initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize()
  {
    this.frame = new JFrame();
    frame.setResizable(false);
    this.frame.setBounds(100, 100, 297, 226);
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.getContentPane().setLayout(null);

    final JLabel lblNewLabel = new JLabel("\u8D26\u53F7");
    lblNewLabel.setBounds(10, 26, 39, 31);
    this.frame.getContentPane().add(lblNewLabel);

    this.textField = new JTextField();
    this.textField.setBounds(47, 26, 171, 31);
    this.frame.getContentPane().add(this.textField);
    this.textField.setColumns(10);
    this.textField.setText(Global.INSTANCE.config.getConfig().qq + "");

    final JLabel lblNewLabel_1 = new JLabel("\u5BC6\u7801");
    lblNewLabel_1.setBounds(10, 63, 39, 31);
    this.frame.getContentPane().add(lblNewLabel_1);

    this.passwordField = new JPasswordField();
    this.passwordField.setBounds(47, 67, 171, 31);
    this.frame.getContentPane().add(this.passwordField);
    this.passwordField.setText(Global.INSTANCE.config.getConfig().pass);

    final JLabel label = new JLabel("\u6FC0\u6D3B\u7FA4\u5217\u8868");
    label.setBounds(10, 111, 75, 15);
    this.frame.getContentPane().add(label);

    this.textField_1 = new JTextField();
    this.textField_1.setBounds(89, 108, 153, 21);
    this.frame.getContentPane().add(this.textField_1);
    this.textField_1.setColumns(10);
    final StringBuilder builder = new StringBuilder();
    Global.INSTANCE.config.getConfig().gp.stream().map(i -> i + ",").forEach(builder::append);
    this.textField_1.setText(builder.substring(0, (builder.length() - 1) >= 0 ? (builder.length() - 1) : 0));

    final JButton btnLogin = new JButton("Login");
    btnLogin.addActionListener(e ->
    {
      Global.INSTANCE.bot = BotFactoryJvm.newBot(Long.parseLong(this.textField.getText()),
          new String(this.passwordField.getPassword()), new BotConfiguration()
          {
            {
              this.setDeviceInfo(context -> SystemDeviceInfoKt.loadAsDeviceInfo(new File("deviceInfo.json"), context));
            }
          });
      Global.INSTANCE.bot.login();
      new Thread(() ->
      {
        Global.INSTANCE.bot.join();
        System.exit(0);
      })
      {
        {
          this.setDaemon(true);
        }
      }.start();
      Global.INSTANCE.config.getConfig().gp = Arrays
          .asList(this.textField_1.getText().contains(",") ? this.textField_1.getText().split(",")
              : new String[] { this.textField_1.getText() })
          .stream().map(Long::parseLong).collect(Collectors.toList());
      Global.INSTANCE.config.getConfig().pass = new String(this.passwordField.getPassword());
      Global.INSTANCE.config.getConfig().qq = Long.parseLong(this.textField.getText());
      try {
        Global.INSTANCE.config.saveConfig();
        Global.INSTANCE.fout = new FileOutputStream("./"
            + LocalDateTime.now().format(
                new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter(Locale.SIMPLIFIED_CHINESE))
            + ".log", true);
      } catch (final Throwable e2) {
        Global.INSTANCE.ct(e2);
      }
      Main.this.frame.setVisible(false);
      Main.this.frame.setEnabled(false);
      LogWin.main(new String[0]);
    });
    btnLogin.setBounds(80, 154, 93, 23);
    this.frame.getContentPane().add(btnLogin);

  }
}
