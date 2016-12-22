package inspectgl;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Vector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLI {
	
	/** main cli entrypoint */
	public static void main(String[] parArgs) {
		PrintWriter stdWriter = new PrintWriter(System.out, true);
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLineParser cliParser = new DefaultParser();
		
		Options options = CLIOptions.buildOptions();
		
		Config.Builder configBuilder = new Config.Builder();
		
		// parse and interpret cli args
		try {
			CommandLine cl = cliParser.parse(options, parArgs);
			
			// interpret args
			interpretArgs(cl, configBuilder);
		}
		catch(Exception e) {
			// print usage
			System.out.println("error: " + e.getMessage());
			helpFormatter.printUsage(stdWriter, 80, "inspectgl", options);
			return;
		}
		
		// finally, run inspectgl; gather info
		try {
			Config config = configBuilder.build();
			InspectGL.run(config);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
//		helpFormatter.printUsage(stdWriter, 80, "inspectgl", options);
	}
	
	public static final void interpretArgs(CommandLine cl, Config.Builder parBuilder) throws IllegalArgumentException {
		// outputfile
		File outputFile = new File(cl.getOptionValue("o"));
		if(outputFile.isDirectory()) throw new IllegalArgumentException("invalid output file: file is directory");
		if(outputFile.exists() && !outputFile.canWrite()) throw new IllegalArgumentException("invalid output file: cannot write");
		parBuilder.outputFile(outputFile);
		
		// output encoding
		String outputEncodingString = cl.getOptionValue("e", "UTF-8");
		try {
			parBuilder.outputEncoding(Charset.forName(outputEncodingString));
		}
		catch(Exception e) {
			throw new IllegalArgumentException("unknown or unsupported encoding: " + outputEncodingString);
		}
		
		// gather
		Vector<InfoType> infoTypeList = new Vector<InfoType>();
		
		String gatherInfoString = cl.getOptionValue("g");
		String[] splitGatherInfoString = gatherInfoString.split(",");
		
		for(int i = 0; i < splitGatherInfoString.length; i++) {
			String tempInfo = splitGatherInfoString[i].trim();
			
			InfoType tempInfoType = InfoType.parse(tempInfo);
			if(tempInfoType == null) throw new IllegalArgumentException("unknown infotype: " + tempInfo);
			infoTypeList.addElement(tempInfoType);
		}
		parBuilder.gatherInfoTypes(infoTypeList.toArray(new InfoType[infoTypeList.size()]));
		
		// prettyjson
		parBuilder.prettyJSON(cl.hasOption("prettyjson"));
	}
	
	public static final class CLIOptions {
		private static Vector<Option> optionList = new Vector<Option>(32);
		
		public static final Option optionOutputFile = 		reg(Option.builder("o").longOpt("outputfile").required().type(String.class).argName("file").hasArg().numberOfArgs(1).optionalArg(false).desc("path of the output file").build());
		public static final Option optionOutputEncoding = 	reg(Option.builder("e").longOpt("encoding").type(String.class).hasArg().numberOfArgs(1).optionalArg(false).desc("encoding of the output file").build());
		public static final Option optionGather = 			reg(Option.builder("g").longOpt("gather").required().type(String.class).argName("info").hasArg().numberOfArgs(1).optionalArg(false).desc("comma seperated list of information to gather").build());
		public static final Option optionPrettyJSON = 		reg(Option.builder("prettyjson").hasArg(false).optionalArg(false).desc("whether to output pretty json").build());
		
		private static Option reg(Option parOption) {
			optionList.addElement(parOption);
			return parOption;
		}
		
		public static Options buildOptions() {
			Options options = new Options();
			for(Option o : optionList) {
				options.addOption(o);
			}
			return options;
		}
	}
}
