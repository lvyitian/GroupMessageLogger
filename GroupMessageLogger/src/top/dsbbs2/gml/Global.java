
package top.dsbbs2.gml;

import java.io.FileOutputStream;

import net.mamoe.mirai.Bot;
import top.dsbbs2.gml.config.SimpleConfig;
import top.dsbbs2.gml.config.struct.Settings;

public final class Global
{
  public static final Global INSTANCE = new Global();

  private Global()
  {
  }
  public FileOutputStream fout;
  public Main main;
  public Bot bot;
  public SimpleConfig<Settings> config = new SimpleConfig<Settings>("./settings.json", "UTF8", Settings.class)
  {
    {
      try {
        this.loadConfig();
      } catch (final Throwable e) {
        Global.this.ct(e);
      }
    }
  };

  public void ct(final Throwable e)
  {
    if (e instanceof RuntimeException) {
      throw (RuntimeException) e;
    } else if (e instanceof Error) {
      throw (Error) e;
    } else {
      throw new RuntimeException(e);
    }
  }
}
