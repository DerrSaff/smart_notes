package com.test.smartnotes.database;

/**
 * Created by saff on 09.06.17.
 */
public class NoteData {

    private long id;
    private String noteTitle;
    private String noteText;
    private int importance;
    private String imagePath;
    private double latitude;
    private double longitude;


    /**
     * Empty constructor
     */
    public NoteData(){

    }

    /**
     * Note Constructor  @param id the id
     *
     * @param id         the id
     * @param noteTitle  the note title
     * @param noteText   the note text
     * @param importance the importance
     * @param imagePath  the image path
     * @param latitude   the latitude
     * @param longitude  the longitude
     */
    public NoteData(long id, String noteTitle, String noteText, int importance, String imagePath,
                    double latitude, double longitude) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.importance = importance;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Note Constructor  @param noteTitle the note title
     *
     * @param noteTitle  the note title
     * @param noteText   the note text
     * @param importance the importance
     * @param imagePath  the image path
     * @param latitude   the latitude
     * @param longitude  the longitude
     */
    public NoteData(String noteTitle, String noteText, int importance, String imagePath,
                    double latitude, double longitude) {
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.importance = importance;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Get id int.
     *
     * @return the int
     */
    public long getID(){
        return this.id;
    }

    /**
     * Set id.
     *
     * @param id the id
     */
    public void setID(long id){
        this.id = id;
    }

    /**
     * Get Note Title string.
     *
     * @return the string
     */
    public String getNoteTitle(){
        return this.noteTitle;
    }

    /**
     * Set Note Title.
     *
     * @param noteTitle the note title
     */
    public void setNoteTitle(String noteTitle){
        this.noteTitle = noteTitle;
    }

    /**
     * Get Note Text string.
     *
     * @return the string
     */
    public String getNoteText(){
        return this.noteText;
    }

    /**
     * Set Note Text.
     *
     * @param noteText the note text
     */
    public void setNoteText(String noteText){
        this.noteText = noteText;
    }

    /**
     * Get Note Importance int.
     *
     * @return int int
     */
    public int getImportance(){
        return this.importance;
    }

    /**
     * Set Note Importance int.
     *
     * @param importance int
     */
    public void setImportance(int importance){
        this.importance = importance;
    }

    /**
     * Get Note Image Path string.
     *
     * @return the string
     */
    public String getImagePath(){
        return this.imagePath;
    }

    /**
     * Set Note Image Path string.
     *
     * @param imagePath the note text
     */
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    /**
     * Get Note Latitude double.
     *
     * @return double double
     */
    public double getLatitude(){
        return this.latitude;
    }

    /**
     * Set Note Importance double.
     *
     * @param latitude double
     */
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    /**
     * Get Note Longitude double.
     *
     * @return double double
     */
    public double getLongitude(){
        return this.longitude;
    }

    /**
     * Set Note Longitude double.
     *
     * @param longitude double
     */
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
}