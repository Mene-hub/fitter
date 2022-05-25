package com.fitterAPP.fitter.Classes

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
    }
}

interface IImageName
{

    fun getname(): String

}