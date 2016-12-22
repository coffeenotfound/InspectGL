package inspectgl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

/** Main logic of InspectGL, called from the CLI entrypoint */
public class InspectGL {
	public static final String INSPECTGL_VERSION = "0.1.0";
	
	private static Config config;
	
	public static void run(Config parConfig) throws Exception {
		config = parConfig;
		
		// poke classes
		OpenGLInspector.poke();
		
//		Infoset infoset = new Infoset();
		JsonObject infoset = new JsonObject();
		
		// collect InspectGL info
		collectBasicInfo(infoset);
		
		// collect platform info
		collectPlatformInfo(infoset);
		
		// collect gl info
		OpenGLInspector.collectGLInfo(infoset);
		
		// write collected data to file
		try {
			writeOutputFile(infoset);
		}
		catch(Exception e) {
			throw new IOException("could not write output file", e);
		}
	}
	
	private static void collectBasicInfo(JsonObject parInfoset) {
		// collect uuid
		byte[] uuid = Utils.generateUUID();
		parInfoset.addProperty("uuid", Utils.byteArrayToHexString(uuid));
		
		// collect utc timestamp
		Instant timestampNow = Instant.now();
		FastDateFormat timestampDateFormat = FastDateFormat.getInstance(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern(), TimeZone.getTimeZone("UTC"));
		String timestampISO = timestampDateFormat.format(Date.from(timestampNow));
		parInfoset.addProperty("timestampISO8601", timestampISO);
		
		// basic info
		parInfoset.addProperty("inspectglversion", INSPECTGL_VERSION);
		parInfoset.addProperty("encoding", config.outputEncoding.name());
	}
	
	private static void collectPlatformInfo(JsonObject parInfoset) {
		JsonObject platformGroup = new JsonObject();
		
		platformGroup.add("NOTIMPLEMENTEDYET", JsonNull.INSTANCE);
		
		// os info
		JsonObject osGroup = new JsonObject();
		platformGroup.add("os", osGroup);
		osGroup.addProperty("name", System.getProperty("os.name"));
		osGroup.addProperty("version", System.getProperty("os.version"));
		osGroup.addProperty("arch", System.getProperty("os.arch"));
		
		// add to infoset
		parInfoset.add("platform", platformGroup);
	}
	
	private static void writeOutputFile(JsonObject parInfoset) throws IOException {
		// build gson json serializer
		GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
		if(config.prettyJSON) gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		
		// write to file
		Writer fileWriter = null;
		try {
			fileWriter = new OutputStreamWriter(new FileOutputStream(config.outputFile, false), config.outputEncoding);
			
			// write serialized json
			JsonWriter jsonWriter = gson.newJsonWriter(fileWriter);
			if(config.prettyJSON) jsonWriter.setIndent("	");
			
			gson.toJson(parInfoset, jsonWriter);
			
			// flush writer
			fileWriter.flush();
		}
		finally { // stay backwards compatible - no try-with-resources
			Utils.tryClose(fileWriter);
		}
	}
}
