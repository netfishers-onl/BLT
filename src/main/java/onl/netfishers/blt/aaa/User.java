package onl.netfishers.blt.aaa;

import java.security.Principal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class User implements Principal {

	private static Logger aaaLogger = LoggerFactory.getLogger("AAA");

	private static BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

	private static String hash(String password) {
		return passwordEncryptor.encryptPassword(password);
	}
	
	public static final int LEVEL_ADMIN = 15;
	public static final int LEVEL_READWRITE = 3;
	public static final int LEVEL_READONLY = 1;
	public static int MAX_IDLE_TIME = 1800;

	private static long idGenerator = 0;

	private long id = 0;
	private String name;
	private String hashedPassword;
	private int level;
	private boolean remote = false;

	protected User() {

	}

	public User(String name, String password, int level) {
		this.name = name;
		this.setPassword(password);
		this.level = level;
	}
	
	public User(String name, int level) {
		this.name = name;
		this.level = level;
		this.remote = true;
	}

	@XmlAttribute
	public long getId() {
		if (id == 0) {
			id = ++idGenerator;
		}
		return id;
	}

	public void setId(long id) {
		this.id = id;
		if (id > idGenerator) {
			idGenerator = id;
		}
	}

	@XmlElement
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public void setPassword(String password) {
		this.hashedPassword = hash(password);
	}

	public boolean authenticate(String password) {
		if (passwordEncryptor.checkPassword(password, hashedPassword)) {
			aaaLogger.info(MarkerFactory.getMarker("AAA"), "The user {} passed local authentication (permission level {}).", name, level);
			return true;
		}
		else {
			aaaLogger.warn(MarkerFactory.getMarker("AAA"), "The user {} failed local authentication.", name);
			return false;
		}
	}

	@XmlElement
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	@XmlElement
	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}
	
	@XmlElement
	public int getMaxIdleTime() {
		return MAX_IDLE_TIME;
	}
	
	public void setMaxIdleTime(int max) {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

}
