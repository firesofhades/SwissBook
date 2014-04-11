package tk.mapzcraft.firesofhades.SwissBook;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SwissBook extends JavaPlugin {
	File manualFile;
	private final SwissBookPlayerListener playerListener = new SwissBookPlayerListener(
			this);
	public FileConfiguration manual;
	public static final Logger log = Logger.getLogger("Minecraft");
	public void onEnable() {
		getServer().getPluginManager().registerEvents(playerListener, this);
		getCommand("swissbook").setExecutor(new SwissBookCommand(this));
		getCommand("manual").setExecutor(new SwissBookCommand(this));
	manualFile = new File(getDataFolder(), "manual.yml");
	 try {
	        firstRun();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	 manual = new YamlConfiguration();
	    loadYamls();
	    if(!manual.contains("oldManuals")){
	    	List<String> om = new ArrayList<String> ();
	    	om.add("exampleTitle");
	    	manual.createSection("oldManuals");
	    	manual.set("oldManuals", om);
	    	saveYamls();
	    }
	    
	}

	public void onDisable() {

	}
	private void firstRun() throws Exception {
	    if(!manualFile.exists()){
	        manualFile.getParentFile().mkdirs();
	        copy(getResource("manual.yml"), manualFile);
	    }
	}
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void saveYamls() {
	    try {
	        manual.save(manualFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void loadYamls() {
	    try {
	        manual.load(manualFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
