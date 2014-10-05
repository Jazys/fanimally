package fr.jacquemet.fanimally.bdd;



import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;



public class FanimallyContentProvider extends ContentProvider {

  // base de données
	private BaseSQL database;

	public static final String PROVIDER_NAME ="fr.jacquemet.fanimally.provider.sql";
	
	public static final Uri CONTENT_URI_ANIMAL = 
		    Uri.parse("content://"+ PROVIDER_NAME + "/"+Table_Animal.TABLENAME);
	
	public static final Uri CONTENT_URI_RDV = 
		    Uri.parse("content://"+ PROVIDER_NAME + "/"+Table_RDV.TABLENAME);
	
	public static final Uri CONTENT_URI_COURBE = 
		    Uri.parse("content://"+ PROVIDER_NAME + "/"+Table_COURBE.TABLENAME);	
	
	public static final Uri CONTENT_URI_VETO = 
		    Uri.parse("content://"+ PROVIDER_NAME + "/"+Table_VETO.TABLENAME);

	private static final int ANIMAL_TABLE = 1;
	private static final int RDV_TABLE = 2;
	private static final int COURBE_TABLE = 3;
	private static final int VETO_TABLE = 4;
	

	
	private static final UriMatcher uriMatcher;
	static {
	    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	    uriMatcher.addURI(PROVIDER_NAME, Table_Animal.TABLENAME, ANIMAL_TABLE);	   	
	    uriMatcher.addURI(PROVIDER_NAME, Table_RDV.TABLENAME, RDV_TABLE);
	    uriMatcher.addURI(PROVIDER_NAME, Table_COURBE.TABLENAME, COURBE_TABLE);    
	    uriMatcher.addURI(PROVIDER_NAME, Table_VETO.TABLENAME, VETO_TABLE); 
	}

  @Override
  public boolean onCreate() {
    database = new BaseSQL(getContext(),null,null,0);
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Utiliser SQLiteQueryBuilder à la place de la méthode query()
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // vérifier si l'appelant a demandé une colonne qui n'existe pas
    //checkColumns(projection);

    int uriType = uriMatcher.match(uri);
    
    System.out.println(uriType);
    switch (uriType) 
    {
	    case ANIMAL_TABLE:
	    	 queryBuilder.setTables(Table_Animal.TABLENAME);
	      break;	    
	    case RDV_TABLE:
	    	 queryBuilder.setTables(Table_RDV.TABLENAME);
	      break;
	    case COURBE_TABLE:
	    	 queryBuilder.setTables(Table_COURBE.TABLENAME);
	      break;
	    case VETO_TABLE: 
	    	 queryBuilder.setTables(Table_VETO.TABLENAME);
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // assurez-vous que les écouteurs potentiels seront notifiés
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = uriMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
  
    System.out.println(uriType);
    System.out.println(uri.toString());
    
    long id = 0;
    switch (uriType) 
    {
	    case ANIMAL_TABLE:
	      id = sqlDB.insert(Table_Animal.TABLENAME, null, values);
	      getContext().getContentResolver().notifyChange(uri, null);
	      return Uri.parse(CONTENT_URI_ANIMAL + "/" + id);	    
	    case RDV_TABLE:
	    	 id = sqlDB.insert(Table_RDV.TABLENAME, null, values);
	    	 getContext().getContentResolver().notifyChange(uri, null);
	    	    return Uri.parse(CONTENT_URI_RDV + "/" + id);
	    case COURBE_TABLE:
	    	 id = sqlDB.insert(Table_COURBE.TABLENAME, null, values);
	    	 getContext().getContentResolver().notifyChange(uri, null);
	    	    return Uri.parse(CONTENT_URI_COURBE + "/" + id);
	    case VETO_TABLE:
	    	 id = sqlDB.insert(Table_VETO.TABLENAME, null, values);
	    	 getContext().getContentResolver().notifyChange(uri, null);
	    	    return Uri.parse(CONTENT_URI_VETO + "/" + id);
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = uriMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case ANIMAL_TABLE:
      rowsDeleted = sqlDB.delete(Table_Animal.TABLENAME, selection,
          selectionArgs);
      break;
    /*case TODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(TodoTable.TABLE_TODO,
            TodoTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsDeleted = sqlDB.delete(TodoTable.TABLE_TODO,
            TodoTable.COLUMN_ID + "=" + id 
            + " and " + selection,
            selectionArgs);
      }
      break;*/
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = uriMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case ANIMAL_TABLE:
      rowsUpdated = sqlDB.update(Table_Animal.TABLENAME, 
          values, 
          selection,
          selectionArgs);
      break;
   /* case TODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO, 
            values,
            TodoTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO, 
            values,
            TodoTable.COLUMN_ID + "=" + id 
            + " and " 
            + selection,
            selectionArgs);
      }
      break;*/
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  /*private void checkColumns(String[] projection) {
    String[] available = { TodoTable.COLUMN_CATEGORY,
        TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_DESCRIPTION,
        TodoTable.COLUMN_ID };
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // vérifier si toutes les colonnes demandées sont disponibles
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }*/
}
