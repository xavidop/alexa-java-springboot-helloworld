package com.xavidop.alexa.localization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
   // static variable single_instance of type Singleton
   private static LocalizationManager instance = null;

   // variable of type String
   private Locale currentLocale;

   // private constructor restricted to this class itself
   private LocalizationManager(Locale locale)
   {
       this.setCurrentLocale(locale);
   }

    // private constructor restricted to this class itself
    private LocalizationManager()
    {
    }

    private ResourceBundle bundle;

    public String getMessage(String key) {
        if(bundle == null) {
            bundle = ResourceBundle.getBundle("locales/strings", this.getCurrentLocale());
        }
        return bundle.getString(key);
    }

    public String getMessage(String key, Object ... arguments) {
        return MessageFormat.format(getMessage(key), arguments);
    }

    // static method to create instance of Singleton class
   public static LocalizationManager getInstance(Locale locale)
   {
       if (instance == null){
            instance = new LocalizationManager(locale);
       }

       return instance;
   }

    // static method to create instance of Singleton class
    public static LocalizationManager getInstance()
    {
        if (instance == null){
            instance = new LocalizationManager();
        }

        return instance;
    }

    public Locale getCurrentLocale() {
        return this.currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }
}