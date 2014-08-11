package com.jxd.oa.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jxd.oa.bean.Attachment;
import com.yftools.db.sqlite.FinderLazyLoader;
import com.yftools.db.sqlite.ForeignCollectionLazyLoader;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GsonUtil {
	private String DATETIME_FORMAT="yyyy-MM-dd HH:mm:ss";
	protected Gson gson;

	private GsonUtil(){
        //.excludeFieldsWithModifiers(Modifier.PUBLIC)
        //.excludeFieldsWithoutExposeAnnotation()
		gson = new GsonBuilder()
                .registerTypeAdapter(FinderLazyLoader.class,new FinderLazyLoaderTypeAdapter())
                .registerTypeAdapter(ForeignCollectionLazyLoader.class, new ForeignCollectionLazyLoaderTypeAdapter())
                .registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat(DATETIME_FORMAT).create();
	}
	
	private static class SingletonHolder {
		static final GsonUtil INSTANCE = new GsonUtil();
	}

	
	
	public Gson getGson() {
		return gson;
	}
	
	public static GsonUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}

	class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
		private final DateFormat format = new SimpleDateFormat(DATETIME_FORMAT);

		public JsonElement serialize(Timestamp src, Type arg1, JsonSerializationContext arg2) {
			String dateFormatAsString = format.format(new Date(src.getTime()));
			return new JsonPrimitive(dateFormatAsString);
		}

		public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (!(json instanceof JsonPrimitive)) {
				throw new JsonParseException("The date should be a string value");
			}
			try {
				Date date = format.parse(json.getAsString());
				return new Timestamp(date.getTime());
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}
		}
	}
}
