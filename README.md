# InspectGL 
InspectGL is a small Java based command line tool for collecting information about the system and the OpenGL API.
This is useful for analysing extension support of users or to help debug an application in cases of insufficient OpenGL support, like missing extensions.

There are other tools out there which already to basically the same thing (like [GLView](http://www.realtech-vr.com/glview/) but they seem mostly outdated or lacking in features.
InspectGL is supposed to be extensible to allow collection not only of generic info but very specific info that may be needed for some project.

Right now InspectGL really only collects basic information, but in future it will probably get some JNI/JNA to really dive into platform dependent functions to provide extensive information
about the hardware, OS, OpenGL, and probably even other things (OpenAL, OpenCL, performance-, network-, storage-, periperal-information, etc.).

### What is does
Open a command line, run InspectGL with the desired options and it spits out a bunch of JSON into a file, containing all sorts of information.

### What it's for
If you are a "normal" user you won't find this tool to be of any meaningful use. It's for collecting all sorts of info about the system and it's OpenGL accelerator, which is mainly of interest
when analysing the extension and feature support of user's systems. Basically, if you are making a game or some sort of 3D application
you want to know what the computers of your userbase are capable of in order to tune your engine for best performance.

### What it collects
Right now it only collects some fairly basic information, like
* most OpenGL `getString` info (`GL_VENDOR`, `GL_RENDERER`, etc)
* OpenGL extensions via `GL_EXTENSIONS`
* basic OS info provided by Java's system properties (`os.*`)
* UUID and timestamp

### What's next?
I don't exactly know yet. What I want to add in the forseeable future is
* some form of extensibility
* extensive platform-dependent information gathering through JNI or JNA
