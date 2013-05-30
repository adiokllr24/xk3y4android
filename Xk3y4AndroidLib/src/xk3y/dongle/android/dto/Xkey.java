package xk3y.dongle.android.dto;

import java.io.Serializable;
import java.util.List;

public class Xkey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String guistate;
	private String emergency;
	private String trayState;
	public List<Iso> listeGames;
	public List<Item> listeItems;
	
	public String getGuistate() {
		return guistate;
	}
	public void setGuistate(String guistate) {
		this.guistate = guistate;
	}
	public String getEmergency() {
		return emergency;
	}
	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}
	public String getTrayState() {
		return trayState;
	}
	public void setTrayState(String trayState) {
		this.trayState = trayState;
	}
	public List<Iso> getListeGames() {
		return listeGames;
	}
	public void setListeGames(List<Iso> listeGames) {
		this.listeGames = listeGames;
	}
	public List<Item> getListeItems() {
		return listeItems;
	}
	public void setListeItems(List<Item> listeItems) {
		this.listeItems = listeItems;
	}
	
	
	

	
}
