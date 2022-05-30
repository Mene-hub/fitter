package com.fitterAPP.fitter.classes

enum class CardsCover:IImageName {

    gigachad{
        override fun toString(): String {
            return "gigachad"
        }

        override fun getname(): String{
            return "gigachad face"
        }
    },
    gigachad2{
        override fun toString(): String {
            return "gigachad2"
        }

        override fun getname(): String{
            return "gigachad upper body"
        }

    },
    man{
        override fun toString(): String {
            return "man_bodybuilder"
        }

        override fun getname(): String{
            return "gym barbell man"
        }
    },
    man2{
        override fun toString(): String {
            return "man_bodybuilder2"
        }

        override fun getname(): String{
            return "man doing traction"
        }
    },
    woman{
        override fun toString(): String {
            return "woman_bodybuilder"
        }

        override fun getname(): String{
            return "gym barbell woman"
        }

    },
    woman2{
        override fun toString(): String {
            return "woman_bodybuilder2"
        }

        override fun getname(): String{
            return "squat with gym barrel woman"
        }
    };

    companion object {
        fun getProperty(value: String): CardsCover {
            return when (value){
                gigachad.getname() -> gigachad
                gigachad2.getname() -> gigachad2
                man.getname() -> man
                man2.getname() -> man2
                woman.getname() -> man
                woman2.getname() -> man2


                else -> {woman}
            }
        }

        fun getFromName(value: String): CardsCover {
            return when (value){
                gigachad.toString() -> gigachad
                gigachad2.toString() -> gigachad2
                man.toString() -> man
                man2.toString() -> man2
                woman.toString() -> man
                woman2.toString() -> man2

                else -> {woman}
            }
        }

        fun getFromFileName(value: String): CardsCover {
            return when (value){
                "$gigachad.png" -> gigachad
                "$gigachad2.png" -> gigachad2
                "$man.png" -> man
                "$man2.png" -> man2
                "$woman.png" -> man
                "$woman2.png" -> man2

                else -> {woman}
            }
        }
    }
}

interface IImageName
{

    fun getname(): String

}