package xk3y.dongle.android.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;

import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.LoadingUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FullGameInfo extends Iso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Bitmap banner;
	private Bitmap cover;
	private Bitmap originalCover;
	
	private String summary;
	private String gender;
	private String trailer;
	
	public byte[] imageByteArray;
	public byte[] bannerByteArray;

	
	/** Included for serialization - write this layer to the output stream. */
	private void writeObject(ObjectOutputStream out) throws IOException{
	    out.writeUTF(getTitle());
	    out.writeUTF(getId());
	    if (summary == null) {
	    	out.writeUTF(" ");
	    } else {
	    	out.writeUTF(summary);
	    }
	    if (gender == null) {
	    	out.writeUTF(" ");
	    } else {
	    	out.writeUTF(gender);
	    }
	 
	    if (trailer == null) {
	    	out.writeUTF(" ");
	    } else {
	    	out.writeUTF(trailer);
	    }
	 
	    
	    /*
	    if (ConfigUtils.getConfig().loadBanner()) {
		    String strBanner = " ";
			try {
				if (banner != null) {
					strBanner = ImageUtils.convertImageToBase64(banner);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    out.writeUTF(strBanner);
	    }
	    */
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    originalCover.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    this.imageByteArray = stream.toByteArray();
	 
	    out.writeObject(this.imageByteArray);

	    if (ConfigUtils.getConfig().loadBanner()) {
	    	ByteArrayOutputStream bannerstream = new ByteArrayOutputStream();
		    banner.compress(Bitmap.CompressFormat.PNG, 100, bannerstream);
		    this.bannerByteArray = bannerstream.toByteArray();
		    out.writeObject(this.bannerByteArray);
	    }
	    

	}
	 
	/** Included for serialization - read this object from the supplied input stream. */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
	    
	    setTitle(in.readUTF());
	    setId(in.readUTF());
        summary = in.readUTF();
        gender = in.readUTF();
        trailer = in.readUTF();
        
        /*
        if (ConfigUtils.getConfig().loadBanner()) {
	        String strBanner = in.readUTF();
			try {
				banner = ImageUtils.encodeImageFromBase64(strBanner);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        */
	    this.imageByteArray = (byte[]) in.readObject();
	    originalCover = BitmapFactory.decodeByteArray(this.imageByteArray,
	                                               0, this.imageByteArray.length);

        
	    if (ConfigUtils.getConfig().loadBanner()) {
	    	this.bannerByteArray = (byte[]) in.readObject();
		    banner = BitmapFactory.decodeByteArray(this.bannerByteArray,
		                                               0, this.bannerByteArray.length);
        }
	    
	    
	    try {
			LoadingUtils.addCoverToGame(this, originalCover);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Bitmap getCover() {
		return cover;
	}
	public void setCover(Bitmap cover) {
		this.cover = cover;
	}
	public Bitmap getOriginalCover() {
		return originalCover;
	}
	public void setOriginalCover(Bitmap originalCover) {
		this.originalCover = originalCover;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public Bitmap getBanner() {
		return banner;
	}

	public void setBanner(Bitmap banner) {
		this.banner = banner;
	}
	
	
	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}


	/**
	 * Sort iso by title
	 */
	public static Comparator<FullGameInfo> TitleComparator = new Comparator<FullGameInfo>() {

		public int compare(FullGameInfo iso1, FullGameInfo iso2) {

			String title1 = iso1.getTitle().toUpperCase();
			String title2 = iso2.getTitle().toUpperCase();

			// ascending order
			return title1.compareTo(title2);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};
	
	
}
