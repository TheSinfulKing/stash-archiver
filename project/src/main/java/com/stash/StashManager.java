package com.stash;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.utils.UTILS;

public class StashManager {
    private static StashManager instance = null;

    private static HashMap<String, Scene> StashScenesById = new HashMap<String, Scene>();
	private static HashMap<String, Image> StashImagesById = new HashMap<String, Image>();

    private static HashMap<String, String> StashPerformerIdsByName = new HashMap<String, String>();
	private static HashMap<String, Performer> StashPerformersById = new HashMap<String, Performer>();
    private static Set<Performer> AllPerformers = new HashSet<Performer>();
    private static ArrayList<Performer> AllPerformersList = new ArrayList<Performer>();

	private static HashMap<String, String> StashTagIdsByName = new HashMap<String, String>();
	private static HashMap<String, Tag> StashTagsById = new HashMap<String, Tag>();

	private static HashMap<String, String> StashStudioIdsByName = new HashMap<String, String>();
    private static HashMap<String, Studio> StashStudiosById = new HashMap<String, Studio>();

	private static HashMap<String, Gallery> StashGalleriesById = new HashMap<String, Gallery>();

    private static Set<SceneOrImage> AllStashScenes = new HashSet<SceneOrImage>();
	private static Set<SceneOrImage> AllStashImages = new HashSet<SceneOrImage>();
	private static Set<SceneOrImage> AllScenesAndImages;
    
    private static Connection StashConnection;
    private static String DATABASEPATH;

    private StashManager(String database) {
        init(database);
    }

    public static StashManager GetInstance(String StashDatabasePath) {
        if(instance == null) {
            instance = new StashManager(StashDatabasePath);
        }
        return instance;
    }

    private void init(String database) {
        DATABASEPATH = database;

        long startTime = System.nanoTime();
        System.out.print("Opening Stash DB...");
        // open Stash DB
        openStashDatabaseFile();
        long endTime = System.nanoTime();
        long durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

        startTime = System.nanoTime();
        System.out.print("Querying All Stash Scenes...");
        // lookup all scenes - populates StashScenesById, AllStashScenes
        queryAndCacheAllScenes();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

        startTime = System.nanoTime();
        System.out.print("Querying All Stash Images...");
        // lookup all images - populates StashImagesById, AllStashImages
        queryAndCacheAllImages();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

        startTime = System.nanoTime();
        System.out.print("Querying All Stash Tags...");
        // lookup all tags - populates StashTagsById, StashTagIdsByName
        queryAndCacheAllTags();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

		// lookup all studios - populates StashStudiosById
        startTime = System.nanoTime();
        System.out.print("Querying All Stash Studios...");
		queryAndCacheAllStudios();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));
		
		// lookup all galleries - populates StashGalleriesById
		//queryAndCacheAllGalleries();
		
		// lookup all performers - populates StashPerformerIdsByName, StashPerformersById
        startTime = System.nanoTime();
        System.out.print("Querying All Stash Performers...");
		queryAndCacheAllPerformers();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

        // Complex tasks do more than just fetch data
        startTime = System.nanoTime();
        System.out.print("Querying All Stash PerformerScenes...");
		queryAndCachePerformerScenes();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

        startTime = System.nanoTime();
        System.out.print("Querying All Stash PerformerImages...");
		queryAndCachePerformerImages();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

		
        startTime = System.nanoTime();
        System.out.print("Querying All Stash PerformerTags...");
		queryAndCachePerformerTags();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));


        startTime = System.nanoTime();
        System.out.print("Querying All Stash SceneTags...");
		queryAndCacheSceneTags();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));

        startTime = System.nanoTime();
        System.out.print("Querying All Stash ImageTags...");
        queryAndCacheImageTags();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));
        
        startTime = System.nanoTime();
        System.out.print("Associating all subtags...");
        queryAndCacheTagRelations();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));	
        
        startTime = System.nanoTime();
        System.out.print("Associating all substudios...");
        setupChildrenStudios();
        endTime = System.nanoTime();
        durationInMs = (endTime - startTime) / 1000000;
        System.out.println("Done - " + UTILS.getFormattedTime(durationInMs));
		
		// Tertiary tasks are used for only a few items
		//countGalleriesByStudio(); // adds all gallery ids to respective studios
		//countImagesByStudio(); // adds all image ids to respective studios

		AllScenesAndImages = new HashSet<SceneOrImage>();
		AllScenesAndImages.addAll(AllStashScenes);
		AllScenesAndImages.addAll(AllStashImages);

        System.out.println();
    }

    public static Scene GetSceneById(String id) {
        return StashScenesById.get(id);
    }

    public static Image GetImageById(String id) {
        return StashImagesById.get(id);
    }

    public static Performer GetPerformerById(String id) {
        return StashPerformersById.get(id);
    }

    public static Tag GetTagById(String id) {
        return StashTagsById.get(id);
    }

    public static Set<SceneOrImage> AllImages() {
        return AllStashImages;
    }

    public static Set<SceneOrImage> AllScenes() {
        return AllStashScenes;
    }

    public static Set<SceneOrImage> AllScenesAndImages() {
        return AllScenesAndImages;
    }

    public static Set<Performer> AllPerformers() {
        return AllPerformers;
    }

    public static ArrayList<Performer> AllPeformersList() {
        return AllPerformersList;
    }

    public static Studio GetStudioById(String id) {
        return StashStudiosById.get(id);
    }

/*---------------- Stash Query Functions --------------- */
private static void openStashDatabaseFile() {
    try {
        Class.forName("org.sqlite.JDBC");
        StashConnection = DriverManager.getConnection("jdbc:sqlite:" + DATABASEPATH);
    } catch (Exception e) {
        System.out.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheAllScenes() {
    Statement stmt = null;
    // lookup Scene by its fileId
    HashMap<String, String> ScenesByFileId = new HashMap<String, String>();
    HashMap<String, String> PathsByFolderId = new HashMap<String, String>();
    
    // First, query `scenes` for base information
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,title,details,url,date,rating,studio_id,o_counter,organized,created_at,updated_at FROM scenes;");		
        
        while (rs.next()) {
            String stashid = rs.getString("id");
            String title = rs.getString("title");
            String url = rs.getString("url");
            String dateString = rs.getString("date");
            String ratingString = rs.getString("rating");
            int rating = -1;
            if (ratingString != null && !ratingString.isBlank()) {
                rating = Integer.parseInt(ratingString);
            }
            String studio_id = rs.getString("studio_id");
            int o_counter = rs.getInt("o_counter");
            boolean organized = rs.getBoolean("organized");
            String details = rs.getString("details");
            String created_at = rs.getString("created_at");
            String updated_at = rs.getString("updated_at");

            LocalDate date = UTILS.ConvertToDate(dateString);
            LocalDateTime createdDateTime = UTILS.ConvertToDateTime(created_at);
            LocalDateTime updatedDateTime = UTILS.ConvertToDateTime(updated_at);
            
            Scene s = new Scene(stashid, title, url, date, rating, studio_id, o_counter,
                    organized, details, createdDateTime, updatedDateTime);
            StashScenesById.put(stashid, s);
            AllStashScenes.add(new SceneOrImage(s));
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
    
    // Next, query `scenes_files` for fileId
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM scenes_files;");		
        
        while (rs.next()) {
            String sceneId = rs.getString("scene_id");
            String fileId = rs.getString("file_id");
            boolean primary = rs.getBoolean("primary");

            // Don't need duplicate files, only log primary
            if(primary) {
                StashScenesById.get(sceneId).setFileId(fileId);
                ScenesByFileId.put(fileId, sceneId);
            }
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
    
    // Next, use fileId to query `video_files`
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT file_id,duration,video_codec,format,audio_codec,width,height,frame_rate,bit_rate FROM video_files;");		
        
        while (rs.next()) {
            String fileId = rs.getString("file_id");
            Double duration = rs.getDouble("duration");
            String videoCodec = rs.getString("video_codec");
            String format = rs.getString("format");
            String audioCodec = rs.getString("audio_codec");
            Integer width = rs.getInt("width");
            Integer height = rs.getInt("height");
            String framerate = rs.getString("frame_rate");
            String bitrate = rs.getString("bit_rate");
                            
            Dimensions d = new Dimensions(width, height);

            // use ScenesByFileId to help here
            String sceneId = ScenesByFileId.get(fileId);
            Scene s = StashScenesById.get(sceneId);
            if(s == null) {
                // not a primary file 
                continue;
            }
            s.setVideoFilesVars(duration, format, videoCodec, audioCodec, d, framerate, bitrate);
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }

    // Next, query 'folders' to get path
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,path FROM folders;");		
        
        while (rs.next()) {
            String folderId = rs.getString("id");
            String path = rs.getString("path");

            PathsByFolderId.put(folderId, path);
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
    
    // Next, query `files` to get parentFolderId and others
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,basename,parent_folder_id,size FROM files;");		
        
        while (rs.next()) {
            String fileId = rs.getString("id");
            String basename = rs.getString("basename");
            String parentFolderId = rs.getString("parent_folder_id");
            String size = rs.getString("size");

            // use ScenesByFileId to help here
            String sceneId = ScenesByFileId.get(fileId);
            Scene s = StashScenesById.get(sceneId);
            if(s == null) {
                continue;
            }
            String folderPath = PathsByFolderId.get(parentFolderId);
            s.setFilesVars1(basename, folderPath, parentFolderId, size);
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }

    // Next, query 'files_fingerprints' to get hash, checksum, phash
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT file_id, type, fingerprint FROM files_fingerprints;");		
        
        while (rs.next()) {
            String fileId = rs.getString("file_id");
            String type = rs.getString("type");
            String fingerprint = rs.getString("fingerprint");

            String sceneId = ScenesByFileId.get(fileId);
            Scene s = StashScenesById.get(sceneId);
            if(s == null) {
                continue;
            }
            IdentifierType it = IdentifierTypeUtils.GetIndentifierType(type);
            switch(it) {
                case CHECKSUM:
                    s.setChecksum(fingerprint);
                break;
                case HASH:
                    s.setHash(fingerprint);
                break;
                case PHASH:
                    s.setPHash(fingerprint);
                break;
            }
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheAllImages() {
    Statement stmt = null;
    // lookup Image by its fileId
    HashMap<String, String> ImagesByFileId = new HashMap<String, String>();
    HashMap<String, String> PathsByFolderId = new HashMap<String, String>();

    // First, query for `images` information
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,title,rating,studio_id,o_counter,organized,created_at,updated_at FROM images;");

        // checksum
        
        // path obtainable by `images_files` to get fileId, then `files` for basename
        // and folderId, `folders` for path
        // also width, height, format, size

        while (rs.next()) {
            String stashid = rs.getString("id");
            String title = rs.getString("title");
            int rating = rs.getInt("rating");
            String studio_id = rs.getString("studio_id");
            int o_counter = rs.getInt("o_counter");
            boolean organized = rs.getBoolean("organized");
            String created_at = rs.getString("created_at");
            String updated_at = rs.getString("updated_at");

            LocalDateTime createdDateTime = UTILS.ConvertToDateTime(created_at);
            LocalDateTime updatedDateTime = UTILS.ConvertToDateTime(updated_at);

            Image i = new Image(stashid, title, rating, studio_id, o_counter,
                    organized, createdDateTime, updatedDateTime);
            StashImagesById.put(stashid, i);
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }

    // Next, query `images_files` for fileId
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM images_files;");		
        
        while (rs.next()) {
            String imageId = rs.getString("image_id");
            String fileId = rs.getString("file_id");
            boolean primary = rs.getBoolean("primary");

            // Don't need duplicate files, only log primary
            if(primary) {
                StashImagesById.get(imageId).setFileId(fileId);
                ImagesByFileId.put(fileId, imageId);
            }
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }

    // Next, use fileId to query `image_files`
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT file_id,format,width,height FROM image_files;");		
        
        while (rs.next()) {
            String fileId = rs.getString("file_id");
            String format = rs.getString("format");
            Integer width = rs.getInt("width");
            Integer height = rs.getInt("height");
                            
            Dimensions d = new Dimensions(width, height);

            // use ScenesByFileId to help here
            String imageId = ImagesByFileId.get(fileId);
            Image i = StashImagesById.get(imageId);
            if(i == null) {
                continue;
            }
            i.setImageFilesVars(format, d);
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }

        // Next, query 'folders' to get path
        try {
            StashConnection.setAutoCommit(false);
    
            stmt = StashConnection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT id,path FROM folders;");		
            
            while (rs.next()) {
                String folderId = rs.getString("id");
                String path = rs.getString("path");
    
                PathsByFolderId.put(folderId, path);
            }	
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(-1);
        }

    // Next, query `files` to get parentFolderId and others
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,basename,parent_folder_id,size FROM files;");		
        
        while (rs.next()) {
            String fileId = rs.getString("id");
            String basename = rs.getString("basename");
            String parentFolderId = rs.getString("parent_folder_id");
            String size = rs.getString("size");

            // use ScenesByFileId to help here
            String imageId = ImagesByFileId.get(fileId);
            String folderPath = PathsByFolderId.get(parentFolderId);
            Image i = StashImagesById.get(imageId);
            if(i == null) {
                continue;
            }
            i.setFilesVars1(basename, folderPath, parentFolderId, size);
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }

    // Next, query 'files_fingerprints' to get hash, checksum, phash
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT file_id, type, fingerprint FROM files_fingerprints;");		
        
        while (rs.next()) {
            String fileId = rs.getString("file_id");
            String type = rs.getString("type");
            String fingerprint = rs.getString("fingerprint");

            String imageId = ImagesByFileId.get(fileId);
            Image i = StashImagesById.get(imageId);
            if(i == null) {
                continue;
            }
            IdentifierType it = IdentifierTypeUtils.GetIndentifierType(type);
            switch(it) {
                case CHECKSUM:
                    i.setChecksum(fingerprint);
                    break;
                case HASH:
                    System.err.println("ERROR: Images don't have Hashs! - Investigate this!");
                    break;
                case PHASH:
                    System.err.println("ERROR: Images don't have PHashs! - Investigate this!");
                    break;
            }
            AllStashImages.add(new SceneOrImage(i));
        }	
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheAllTags() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id,name FROM tags;");
        
        while (rs.next()) {
            String tagId = rs.getString("id");
            String name = rs.getString("name");
            
            Tag t = new Tag(tagId, name);
            StashTagsById.put(tagId, t);
            StashTagIdsByName.put(name.toUpperCase(), tagId);
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheAllStudios() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,checksum,name,url,parent_id,details,rating FROM studios;");

        while (rs.next()) {
            String stashid = rs.getString("id");
            String parentid = rs.getString("parent_id");
            String checksum = rs.getString("checksum");
            String name = rs.getString("name");
            int rating = rs.getInt("rating");
            String url = rs.getString("url");
            String details = rs.getString("details");

            Studio s = new Studio(stashid, checksum, name, url, parentid, details, rating);
            StashStudiosById.put(stashid, s);
            StashStudioIdsByName.put(name.toUpperCase(), stashid);
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheAllGalleries() {
    Statement stmt = null;
    HashMap<String, String> GalleriesByFolderId = new HashMap<String, String>();
    
    // First, query `galleries` for base
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,folder_id,title,url,date,details,studio_id,rating,organized,created_at,updated_at FROM galleries;");

        while (rs.next()) {
            String id = rs.getString("id");
            String folderId = rs.getString("folder_id");
            String title = rs.getString("title");
            String url = rs.getString("url");
            String date = rs.getString("date");
            String details = rs.getString("details");
            String studio_id = rs.getString("studio_id");
            int rating = rs.getInt("rating");
            boolean organized = rs.getBoolean("organized");
            String created_at = rs.getString("created_at");
            String updated_at = rs.getString("updated_at");
            
            Gallery g = new Gallery(id, folderId, title, url, date, details, studio_id, rating, organized, created_at, updated_at);
            StashGalleriesById.put(id, g);
            GalleriesByFolderId.put(folderId, id);
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }	

    // Next, fetch path from `folders`
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,path FROM folders;");

        // path
        // checksum
        // zip

        // fetch path by
        // query folders for path
        // may be zip

        while (rs.next()) {
            String folderId = rs.getString("id");
            String path = rs.getString("path");
            
            String galleryId = GalleriesByFolderId.get(folderId);
            Gallery g = StashGalleriesById.get(galleryId);
            if(g == null) {
                continue;
            }
            g.setPath(path);
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }	
}

private static void queryAndCacheAllPerformers() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT id,name,gender,url,birthdate,ethnicity,country,eye_color,height,measurements,fake_tits,favorite,death_date,hair_color,weight,rating,created_at,updated_at FROM performers;");

        // twitter, instagram, 
        while (rs.next()) {
            String stashid = rs.getString("id");
            //String checksum = rs.getString("checksum");
            String name = rs.getString("name");
            String url = rs.getString("url");
            String birthdateString = rs.getString("birthdate");
            String ethnicity = rs.getString("ethnicity");
            String country = rs.getString("country");
            String eye_color = rs.getString("eye_color");
            String height = rs.getString("height");
            String measurements = rs.getString("measurements");
            String fake_tits = rs.getString("fake_tits");
            boolean favorite = rs.getBoolean("favorite");
            String death_date = rs.getString("death_date");
            String hair_color = rs.getString("hair_color");
            String weight = rs.getString("weight");
            int rating = rs.getInt("rating");
            String created_at = rs.getString("created_at");
            String updated_at = rs.getString("updated_at");

            LocalDate birthdate = UTILS.ConvertToDate(birthdateString);

            Performer p = new Performer(name, stashid, birthdate, ethnicity, country, eye_color,
                    height, measurements, fake_tits, favorite, death_date, hair_color, weight, rating, url, created_at, updated_at);
            StashPerformersById.put(stashid, p);
            StashPerformerIdsByName.put(name.toUpperCase(), stashid);
            AllPerformers.add(p);
            AllPerformersList.add(p);
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}
    
private static void queryAndCachePerformerTags() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM performers_tags ORDER BY performer_id;");
        
        String currentPerformerId = "none";
        ArrayList<String> currentPerformerTags = new ArrayList<String>();
        while (rs.next()) {
            String tagId = rs.getString("tag_id");
            String nextPerformerId = rs.getString("performer_id");

            // check if we are at a new performer
            if(!nextPerformerId.equalsIgnoreCase(currentPerformerId)) {
                // could be the first entry
                if(currentPerformerId.equalsIgnoreCase("none")) {
                    // update current
                    currentPerformerId = nextPerformerId;
                }
                else {
                    // write to current
                    StashPerformersById.get(currentPerformerId).setTagIds(currentPerformerTags.toArray(new String[0]));
                    // clear
                    currentPerformerTags.clear();
                    currentPerformerId = nextPerformerId;
                }
            }
            currentPerformerTags.add(tagId);
        }
        if(!currentPerformerId.equalsIgnoreCase("none")) {
            // write last
            StashPerformersById.get(currentPerformerId).setTagIds(currentPerformerTags.toArray(new String[0]));
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCachePerformerScenes() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM performers_scenes ORDER BY scene_id ASC;");

        String currentSceneId = "none";
        ArrayList<String> currentPerformerIds = new ArrayList<String>();
        while (rs.next()) {
            String sceneId = rs.getString("scene_id");
            String performerId = rs.getString("performer_id");

            if (!sceneId.equals(currentSceneId)) {
                // exclude first
                if (!currentSceneId.equals("none")) {
                    // write and update currentSceneId
                    StashScenesById.get(currentSceneId).setPerformers(currentPerformerIds.toArray(new String[0]));
                    currentPerformerIds.clear();
                }

                // reset
                currentSceneId = sceneId;
            }
            currentPerformerIds.add(performerId);
        }
        if(!currentSceneId.equalsIgnoreCase("none")) {
            // write last scene
            StashScenesById.get(currentSceneId).setPerformers(currentPerformerIds.toArray(new String[0]));
        }

        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCachePerformerImages() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM performers_images ORDER BY image_id ASC;");

        String currentImageId = "none";
        ArrayList<String> currentPerformerIds = new ArrayList<String>();
        while (rs.next()) {
            String imageId = rs.getString("image_id");
            String performerId = rs.getString("performer_id");

            if (!imageId.equals(currentImageId)) {
                // exclude first
                if (!currentImageId.equals("none")) {
                    // write and update currentSceneId
                    StashImagesById.get(currentImageId).setPerformers(currentPerformerIds.toArray(new String[0]));
                    currentPerformerIds.clear();
                }

                // reset
                currentImageId = imageId;
            }
            currentPerformerIds.add(performerId);
        }
        if(!currentImageId.equalsIgnoreCase("none")) {
            // write last scene
            StashImagesById.get(currentImageId).setPerformers(currentPerformerIds.toArray(new String[0]));
        }

        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheTagRelations() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tags_relations ORDER BY parent_id;");
        
        String currentParentId = "none";
        ArrayList<String> currentChildrenIds = new ArrayList<String>();
        while (rs.next()) {
            String parentId = rs.getString("parent_id");
            String childId = rs.getString("child_id");
            currentChildrenIds.add(childId);

            if (!parentId.equals(currentParentId)) {
                // parentId first
                if (!currentParentId.equals("none")) {
                    // write and update currentSceneId
                    StashTagsById.get(currentParentId).setChildrenId(currentChildrenIds);
                }

                // reset
                currentParentId = parentId;
                currentChildrenIds.clear();
            }
        }
        if(!currentParentId.equalsIgnoreCase("none")) {
            // write last
            StashTagsById.get(currentParentId).setChildrenId(currentChildrenIds);
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheSceneTags() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM scenes_tags ORDER BY scene_id;");
        
        String currentSceneId = "none";
        ArrayList<String> currentSceneTags = new ArrayList<String>();
        while (rs.next()) {
            String tagId = rs.getString("tag_id");
            String nextSceneId = rs.getString("scene_id");

            // check if we are at a new performer
            if(!nextSceneId.equalsIgnoreCase(currentSceneId)) {
                // could be the first entry
                if(currentSceneId.equalsIgnoreCase("none")) {
                    // update current
                    currentSceneId = nextSceneId;
                }
                else {
                    // write to current
                    StashScenesById.get(currentSceneId).setTagIds(currentSceneTags.toArray(new String[0]));
                    // clear
                    currentSceneTags.clear();
                    currentSceneId = nextSceneId;
                }
            }
            currentSceneTags.add(tagId);
        }
        if(!currentSceneId.equalsIgnoreCase("none")) {
            // write last
            StashScenesById.get(currentSceneId).setTagIds(currentSceneTags.toArray(new String[0]));
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

private static void queryAndCacheImageTags() {
    Statement stmt = null;
    try {
        StashConnection.setAutoCommit(false);

        stmt = StashConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM images_tags ORDER BY image_id;");
        
        String currentImageId = "none";
        ArrayList<String> currentImageTags = new ArrayList<String>();
        while (rs.next()) {
            String tagId = rs.getString("tag_id");
            String nextImageId = rs.getString("image_id");

            // check if we are at a new performer
            if(!nextImageId.equalsIgnoreCase(currentImageId)) {
                // could be the first entry
                if(currentImageId.equalsIgnoreCase("none")) {
                    // update current
                    currentImageId = nextImageId;
                }
                else {
                    // write to current
                    StashImagesById.get(currentImageId).setTagIds(currentImageTags.toArray(new String[0]));
                    // clear
                    currentImageTags.clear();
                    currentImageId = nextImageId;
                }
            }
            currentImageTags.add(tagId);
        }
        if(!currentImageId.equalsIgnoreCase("none")) {
            // write last
            StashImagesById.get(currentImageId).setTagIds(currentImageTags.toArray(new String[0]));
        }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(-1);
    }
}

/*---------------- Stash Logic Process Functions --------------- */
/* These functions do not query Stash, simply perform an action on dataset*/

private static void setupChildrenStudios() {
    for(Map.Entry<String, Studio> studio : StashStudiosById.entrySet()) {
        String parentStudioId = studio.getValue().getParentId();
        if(parentStudioId != null && !parentStudioId.isBlank()) {
            StashStudiosById.get(parentStudioId).addChildId(studio.getKey());
        }
    }
}

private static void countGalleriesByStudio() {
    for(Map.Entry<String, Gallery> entry : StashGalleriesById.entrySet()) {
        // get studio id
        String studioId = entry.getValue().getStudio_id();
        if(studioId != null) {
            StashStudiosById.get(studioId).addGalleryId(entry.getKey());
        }			
    }		
}

private static void countImagesByStudio() {
    for(Map.Entry<String, Image> entry : StashImagesById.entrySet()) {
        // get studio id
        String studioId = entry.getValue().getStudio_id();
        if(studioId != null) {
            StashStudiosById.get(studioId).addImageId(entry.getKey());
        }			
    }		
}

/*---------------- Utility Functions --------------- */
/* These functions are used primarily for test logic */

public static String[] ConvertPerformerNamesToPerformerIds(String[] performers) {
    String[] performerIds = new String[performers.length];

    for (int i = 0; i < performers.length; i++) {
        String candidate = StashPerformerIdsByName.get(performers[i].toUpperCase());
        if(candidate == null) {
            performerIds[i] = performers[i];
        }
        else {
            performerIds[i] = candidate;
        }
    }

    return performerIds;
}

public static String[] ConvertTagNamesToTagIds(String[] tags) {
    String[] tagIds = new String[tags.length];
    
    for(int i=0;i<tags.length;i++) {
        tagIds[i] = StashTagIdsByName.get(tags[i].toUpperCase());
    }
    
    return tagIds;
}

public static String[] ConvertStudioNamesToStudioIds(String[] studios) {
    String[] studioIds = new String[studios.length];
    
    for(int i=0;i<studios.length;i++) {
        studioIds[i] = StashStudioIdsByName.get(studios[i].toUpperCase());
    }
    
    return studioIds;
}

public static String[] GetSubTagIdsFromTagId(String tagId) {
    return StashTagsById.get(tagId).getChildrenIds();
}

}