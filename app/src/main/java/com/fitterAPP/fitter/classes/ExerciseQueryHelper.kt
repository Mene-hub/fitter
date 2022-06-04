package com.fitterAPP.fitter.classes

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*

/**
 * Exercise query helper which utilizes REST API
 * * the REST API used is under licence GNU Affero General Public License v3.0.
 * Permissions of this strongest copyleft license are conditioned on making available
 * complete source code of licensed works and modifications, which include larger works using a licensed work,
 * under the same license. Copyright and license notices must be preserved. Contributors provide an express grant of patent rights.
 * When a modified version is used to provide a service over a network, the complete source code of the modified version must be made available.
 * For more information about the licence, [visit](https://github.com/wger-project/wger/blob/master/LICENSE.txt).
 *
 * @author Daniel Satriano
 * @since 03/06/2022
 */
class ExerciseQueryHelper {
    companion object{
        private const val EXERCISE_QUERY = "https://wger.de/api/v2/exercise/search/?format=json&term="

        /**
         * This function is used to get the exercise name query from the REST API
         * @author Daniel Satriano
         */
        private fun getExerciseQuery(exerciseName: String): String{
            return EXERCISE_QUERY + exerciseName
        }

        /**
         * * Method to retrieve exercises with a given name, utilizing OkHttp and GSON.
         * For more information about OkHttp and GSON, visit [OkHttp](https://square.github.io/okhttp/), [GSON](https://github.com/google/gson/blob/master/UserGuide.md)
         * @author Daniel Satriano
         * @throws android.os.NetworkOnMainThreadException
         */
        @Throws(android.os.NetworkOnMainThreadException::class)
        fun getExercises(exerciseName: String): Root {
            //get json object from the REST API with exerciseName as a query
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(getExerciseQuery(exerciseName))
                .build()
            val response = client.newCall(request).execute()
            val json = response.body()?.string()

            //convert responseBody to object of ExerciseSuggestions
            val gson = Gson()
            val type = object : TypeToken<Root>() {}.type
            return gson.fromJson(json, type)
        }


        /**
         * Private method which is used to convert Root list of suggestion to a MutableList<Exercise>
         * @author Daniel Satriano
         * @see Exercise for more information
         * @see Root for more information
         */
        fun convertToExercise(exerciseSuggestion: Root) : MutableList<Exercise>{
            val exercises = mutableListOf<Exercise>()
            for(suggestion in exerciseSuggestion.suggestions){
                val exercise = Exercise(suggestion.value, 1, 1, 60.00, listOf(suggestion.data.image_thumbnail, suggestion.data.image))
                exercises.add(exercise)
            }
            return exercises
        }

    }
}

//The classes below are used to parse the JSON response from the REST API inside [ExerciseQueryHelper.getExercises]



/**
 * Root class for the exercise query which contains the suggestions.
 * This class uses an array of [Suggestion] objects to store the suggestions.
 * @author Daniel Satriano
 * @param suggestions the suggestions of the exercise query
 * @see Suggestion for more information
 */
data class Root(var suggestions: List<Suggestion>){
    //toString method which pretty prints the object
    override fun toString(): String {
        return "ExerciseSuggestions(suggestion=$suggestions)"
    }
}

/**
 * Suggestion class for the exercise query which contains the exercise data and the name.
 * This class is used in the [Root] class asnd uses the [Data] class as a data class.
 * @author Daniel Satriano
 * @param value the exercise name
 * @param data the exercise data
 * @see Data for more information
 * @see Root for more information
 */
data class Suggestion(var value: String, var data : Data){
    //toString method which pretty prints the object
    override fun toString(): String {
        return "ExerciseSuggestion(value='$value', data=$data)"
    }

}

/**
 * Data class for the exercise query which contains the exercise information.
 * This class is used in the [Suggestion] class
 * @author Daniel Satriano
 * @param id the exercise id
 * @param name the exercise name
 * @param category the exercise category
 * @param image the exercise image
 * @param image_thumbnail the exercise thumbnail image
 * @see Suggestion for more information
 */
data class Data(var id : Int, var name : String, var category : String, var image : String, var image_thumbnail : String){
    //toString method which pretty prints the object
    override fun toString(): String {
        return "DataSuggestion(id=$id, name='$name', category='$category', image='$image', image_thumbnail='$image_thumbnail')"
    }
}