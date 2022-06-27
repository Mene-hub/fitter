package com.fitterAPP.fitter.classes

object ApiKeyRetriever {
    init{
        System.loadLibrary("api-keys")
    }
    external fun getGoogleKey():String
    external fun getFacebookToken():String
    external fun getDatabase():String
    external fun getFacebookProtocolScheme():String


}