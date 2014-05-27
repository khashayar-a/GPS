package gps.rendering;

public class MapType {
	private String key;
	private String value;
	private String type;
	private boolean isPath;
	private int loop;

	public MapType(String key, String value, String type, boolean isPath, int loop) {
		this.key = key;
		this.value = value;
		this.type = type;
		this.isPath = isPath;
		this.loop = loop;
	}
	
	public boolean isType(String key,String value) {
		if( this.key.equalsIgnoreCase(key) && this.value.equalsIgnoreCase(value))
			return true;
		else
			return false;
	}

	public String getType() {
		return this.type;
	}

	public boolean isPath() {
		return this.isPath;
	}
	
	public int getLoop() {
		return this.loop;
	}
}
