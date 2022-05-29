package com.fitterAPP.fitter.databases

/*
class StorageHelper(private val storage : FirebaseStorage) {
    private val storageRef : StorageReference = storage.reference

    fun uploadUserPicByte(currentUser : Athlete, arrayByte: ByteArray, listener : OnFailureListener ){
        if(!currentUser.UID.isNullOrEmpty()){
            val imageRef : StorageReference = storageRef.child(currentUser.UID).child("profile_pic.jpg")
            imageRef.putBytes(arrayByte).addOnFailureListener(listener)
        }
    }

    fun uploadUserPicFile(currentUser : Athlete, file : Uri, listener : OnFailureListener ){
        if(!currentUser.UID.isNullOrEmpty()){
            val imageRef : StorageReference = storageRef.child(currentUser.UID).child("profile_pic.jpg")
            imageRef.putFile(file).addOnFailureListener(listener)
        }
    }

    fun retriveUserPic(currentUser : Athlete) : ByteArray?{
        val ONE_MEGABYTE : Long = 1024*1024
        var arrayTMP : ByteArray? = null
        if(!currentUser.UID.isNullOrEmpty()){
            val imageRef : StorageReference = storageRef.child(currentUser.UID).child("profile_pic.jpg")
            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                array ->
                arrayTMP = array
            }.addOnFailureListener{
                arrayTMP = null
            }
        }
        return arrayTMP
    }
/*
    fun uploadFile(path : String, arrayByte : ByteArray){
        val imageRef : StorageReference = storageRef.child(path)
        imageRef.putBytes(arrayByte)
    }
*/
}
 */

