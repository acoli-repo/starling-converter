package de.unifrankfurt.informatik.acoli.starling.types;


/**
 * Class for representing bibliographic references taken from
 * the Starling website (see the pages on the following URL)
 * <p>
 * see <a href="http://starling.rinet.ru/cgi-bin/response.cgi?
 * root=config&morpho=0&basename=\data\biblio\biblio.dbf&
 * first=1&off=&text_author=&method_author=substring&ic_author
 * =on&text_title=&method_title=substring&ic_title=on&text_db=
 * &method_db=substring&ic_db=on&text_abbreviati=&method_abbreviati
 * =substring&ic_abbreviati=on&text_publisher=&method_publisher=
 * substring&ic_publisher=on&text_place=&method_place=
 * substring&ic_place=on&text_year=&method_year=substring&ic_year=
 * on&text_any=&method_any=substring&sort=author&ic_any=on">Bibliographic References</a>
 * <p>
 * and the file /input-files/starbib.txt .
 * 
 * @author frank
 *
 */
public class StarlingBibRef {
	
	private int id;
	private String hashKey;
	private String author;
	private String title;
	private String usedBy;
	private String shortName;
	private String place;
	private String year;
	private String url;
	
	
	public StarlingBibRef(int idNr, String hashKey, String author,
			String title, String usedBy, String shortName, String place,
			String year) {
		super();
		this.id = idNr;
		this.hashKey = hashKey;
		this.author = author;
		this.title = title;
		this.usedBy = usedBy;
		this.shortName = shortName;
		this.place = place;
		this.year = year;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUsedBy() {
		return usedBy;
	}
	public void setUsedBy(String usedBy) {
		this.usedBy = usedBy;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getHashKey() {
		return hashKey;
	}
	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	

}
