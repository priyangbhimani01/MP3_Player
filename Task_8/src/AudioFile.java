
 public abstract class AudioFile{

		
		protected String pathname;
		protected String filename;
		protected String author="";
		protected String title="";
		
		public AudioFile() {}
		
		public AudioFile(String path){
			
			parsePathname(path);
	        parseFilename(this.filename);
		}
		
		public String getPathname() {
			return this.pathname;
		}
		
		public String getFilename() {
			return this.filename;
		}
		
		public String getAuthor() {
			return this.author;
		}
		
		public String getTitle() {
			return this.title;
		
		}
		
		public String toString() {
			
	        if (author.isEmpty()) {
	            return this.title;
	        } else {
	        	
	            return this.author + " - " + this.title;
	        }
	   }
		
	
		
		public void parseFilename(String filename) {
			
			
			
		    String[] parts = filename.split(" - ",2);

		    if(parts.length==2) {
		        
		        
		        String RawAuthorName = parts[0];
		        String RawTiltleName= parts[1];
		        
		       
		        author = RawAuthorName.trim();
		        if(!RawTiltleName.contains(".")) {
		            title="";
		        }
		        else {title = RawTiltleName.substring(0, RawTiltleName.lastIndexOf('.')).trim();}
		        
		        
		    }
		    
	         else if(parts.length==0 ) {
		        title="-";
		        author="";
		        
		        
		    }
		    
		    else if(parts[0].contains(".")) {
		        
		         title = parts[0].substring(0, parts[0].lastIndexOf('.')).trim();
		         author="";
		    }
		    else if(filename=="-") {
		        
		        this.author="";
		        this.title="-";
		    }
		    
		    else if(filename=="" || filename==" ") {
		        this.author="";
		        this.title="";
		        
		    }
		    
		    else  {
		    	title="-";
		    	author="";
		    }
		    
		        
		 }
		
		
		public void parsePathname(String path) {
			
			if( path == null || path.trim().isEmpty()) {
				this.pathname="";
				this.filename="";
			}

			
			Boolean IsWindows = System.getProperty("os.name").toLowerCase().contains("win");
			
			if(IsWindows) {
				path = path.replaceAll("/+","\\\\");
				path= path.replaceAll("\\\\+","\\\\");
			}
			else {
				path=path.replaceAll("\\\\+", "/");
				path=path.replaceAll("/+", "/");
				
				if(path.contains(":")) {
					path=path.replaceAll(":","");}
				if(path.indexOf("/")!=0 && path.contains("/")) {
					if(path.contains("home")|| path.contains("../") || path.contains("audiofiles")) {
						
		                   
		                }else {
		                	path = "/" + path;
		                }
				   }
			    }
			
			
			if(!path.contains("\\\\") || !path.contains("/")) {
				path=path.trim();
			}
			
			
			if(path.contains("home")|| path.startsWith("/..")) {
				
				path=path.substring(0,path.length());
				
			}
			
			this.pathname = path;
	
			//code to find file name
			
			
			if (path.isEmpty()) {
				filename="";
			}
			else if (path.charAt(path.length() - 1) == '/' || path.charAt(path.length() - 1) == '\\'){
				filename="";
			}
			else if (path.contains("/") || path.contains("\\")){
				// for windows 
				if(path.contains("\\")) {
					String parts[]=path.split("\\\\");
					
					for(String s : parts ) {
						
						if(s.contains(".") || s.contains("-") ) {
							s=s.trim();
							filename = s;
						}
						}
                 
				} else if (path.contains("/")) {
					String parts[]=path.split("/");
					for(String s : parts ) {
						if(s.contains(".") || s.contains("-")) {
							s=s.trim();
							filename = s;
						}
						}
					}
				} else {
					path=path.trim();
					filename=path;
						}  	
			
			}
		
		 public abstract void play();
			
		
		
		public abstract void togglePause();
			
		
		
		public abstract void stop();
			
		
		
		public abstract String formatDuration();
			
		
		
		public abstract String formatPosition();
			
	
		
		}