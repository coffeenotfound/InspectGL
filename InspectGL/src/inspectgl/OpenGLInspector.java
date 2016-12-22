package inspectgl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import com.google.gson.JsonObject;

/** uses glfw for now */
public class OpenGLInspector {
	
	public static void collectGLInfo(JsonObject parInfoset) throws IllegalStateException {
		try {
			JsonObject openglGroup = new JsonObject();
			
			// init glfw
			try {
				GL.create();
			}
			catch(Exception e) {}
			if(!GLFW.glfwInit()) throw new IllegalStateException();
			
			// create context (and helper window)
			GLFW.glfwDefaultWindowHints();
			GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
//			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
			long contextGLFW = GLFW.glfwCreateWindow(1280, 720, "InspectGL Helper Window", 0L, 0L);
			
			if(contextGLFW == 0L) throw new IllegalStateException("glfw window creation failed");
			
			// make current
			GLFW.glfwMakeContextCurrent(contextGLFW);
			GL.createCapabilities();
			
			// collect basic info
			openglGroup.addProperty("GL_VENDOR", GL11.glGetString(GL11.GL_VENDOR));
			openglGroup.addProperty("GL_RENDERER", GL11.glGetString(GL11.GL_RENDERER));
			openglGroup.addProperty("GL_VERSION", GL11.glGetString(GL11.GL_VERSION));
			openglGroup.addProperty("GL_SHADING_LANGUAGE_VERSION", GL.getCapabilities().OpenGL20 ? GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION) : "null");
			
			// collect extensions
			String extensionsString = GL11.glGetString(GL11.GL_EXTENSIONS);
			openglGroup.addProperty("GL_EXTENSIONS", extensionsString);
//			String[] extensionsSplit = extensionsString.split(" ");
//			JsonArray jsonExtensionArray = new JsonArray();
//			for(int i = 0; i < extensionsSplit.length; i++) {
//				jsonExtensionArray.add(extensionsSplit[i]);
//			}
//			openglGroup.add("GL_EXTENSIONS", jsonExtensionArray);
			
			// add to infoset
			parInfoset.add("gl", openglGroup);
		}
		finally {
			// terminate glfw
			GLFW.glfwTerminate();
		}
	}
	
	public static final void poke() {
		
	}
}
