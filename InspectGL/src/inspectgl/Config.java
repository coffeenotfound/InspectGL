package inspectgl;

import java.io.File;
import java.nio.charset.Charset;

public final class Config {
	public final File outputFile;
	public final Charset outputEncoding;
	public final InfoType[] gatherInfoTypes;
	public final boolean prettyJSON;
	
	public Config(File parOutputFile, Charset parEncoding, InfoType[] parGatherInfoTypes, boolean parPrettyJSON) {
		outputFile = parOutputFile;
		outputEncoding = parEncoding;
		gatherInfoTypes = parGatherInfoTypes;
		prettyJSON = parPrettyJSON;
	}
	
	public static final class Builder {
		private File outputFile;
		private Charset outputEncoding;
		private InfoType[] gatherInfoTypes;
		private boolean prettyJSON;
		
		public Builder outputFile(File parFile) {
			outputFile = parFile;
			return this;
		}
		
		public Builder outputEncoding(Charset parEncoding) {
			outputEncoding = parEncoding;
			return this;
		}
		
		public Builder gatherInfoTypes(InfoType[] parTypes) {
			gatherInfoTypes = parTypes;
			return this;
		}
		
		public Builder prettyJSON(boolean parEnabled) {
			prettyJSON = parEnabled;
			return this;
		}
		
		public Config build() throws IllegalArgumentException {
			if(outputFile == null) throwRequireAttribute("outputFile");
			if(outputEncoding == null) throwRequireAttribute("outputEncoding");
			if(gatherInfoTypes == null) throwRequireAttribute("gatherInfoTypes");
			return new Config(outputFile, outputEncoding, gatherInfoTypes, prettyJSON);
		}
		
		private void throwRequireAttribute(String parAttribute) throws IllegalArgumentException {
			throw new IllegalArgumentException("builder cannot build Config instance: required attribute " + parAttribute + " not set");
		}
	}
}
