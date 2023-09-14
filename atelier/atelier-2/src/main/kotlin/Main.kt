abstract class Personnage(){
    abstract var nom: String
    abstract var att: Int
    abstract var def: Int
    abstract var pv: Int
    abstract var degats: Int

    fun attaquer(defendant: Personnage) {
        val  difficulteDeToucher = att - defendant.def
        val resultatLancerDe = (1..20).random()

        if (resultatLancerDe >= difficulteDeToucher){
            val degatsInfliges = degats
            defendant.pv -= degatsInfliges
            println("$nom attaque avec succès et inflige $degatsInfliges dégâts à ${defendant.nom}!")
        } else {
            println("$nom rate son attaque contre ${defendant.nom}.")
        }
    }

    open fun attaqueSpecial(defendant: Personnage) {
        // Implement special attack logic for each character type
    }

    companion object {
        var isDead: Boolean = false
    }
}

class Guerrier: Personnage() {
    override var nom = "\u001b[34mGuerrier\u001B[0m"
    override var att = 16
    override var def = 2
    override var pv = 10
    override var degats = (1..8).random()

    override fun attaqueSpecial(defendant: Personnage) {
        println("$nom effectue une attaque spéciale automatique et inflige ${degats * 2} dégâts à ${defendant.nom}!")
        defendant.pv -= degats * 2
    }
}

class Mage: Personnage() {
    override var nom = "\u001b[33mMage\u001B[0m"
    override var att = 19
    override var def = 8
    override var pv = 5
    override var degats = (1..4).random()

    override fun attaqueSpecial(defendant: Personnage) {
        val magicDamage = (4..10).random()
        println("$nom lance une attaque magique spéciale et inflige $magicDamage dégâts à ${defendant.nom}!")
        defendant.pv -= magicDamage
    }
}

class Clerc: Personnage() {
    override var nom = "\u001b[32mClerc\u001B[0m"
    override var att = 17
    override var def = 4
    override var pv = 8
    override var degats = (1..6).random()

    override fun attaqueSpecial(defendant: Personnage) {
        val healingAmount = (2..6).random()
        println("$nom utilise une attaque spéciale de guérison et restaure $healingAmount points de vie!")
        if (pv != 8){
            pv += healingAmount
            if (pv > 8){
                pv= 8
            }
        }
    }
}

class Ennemi: Personnage(){
    override var nom = "\u001b[31mGoblin\u001b[0m"
    override var att = 10
    override var def = 5
    override var pv = 10
    override var degats = (1..4).random()
}

fun main(args: Array<String>) {
    var turn = 1
    var specialTurn= 0;
    var decision: Int
    var ennemi: Ennemi? = null

    println("\uD83C\uDFAE Préparez-vous pour l'aventure ! \uD83C\uDF1F\n" +
            "\n" +
            "Choisissez votre destin :\n" +
            "1\uFE0F⃣ Guerrier courageux\n" +
            "2\uFE0F⃣ Mage puissant\n" +
            "3\uFE0F⃣ Clerc sage\n" +
            "\n" +
            "Le voyage vous attend ! ⚔\uFE0F\uD83D\uDD2E\uD83D\uDCDC:")
        val choix: Int = readlnOrNull()?.toInt() ?: 0
        val personnage: Personnage? = when (choix) {
        1 -> Guerrier()
        2 -> Mage()
        3 -> Clerc()
        else -> null
    }

    if (personnage != null) {
        println("Vous avez choisi: ${personnage.nom}")
        ennemi = Ennemi()
        println("Ton Ennemi est: ${ennemi.nom}")

        while (!Personnage.isDead) {
                println("Choisissez une action :\n" +
                        "1. Attaquer\n" +
                        "2. Attaque spéciale\n" +
                        "3. Fuir")
                decision = readlnOrNull()?.toInt() ?: 0
                when (decision) {
                    1 -> {
                        personnage.attaquer(ennemi)
                        ennemi.attaquer(personnage) // Enemy attacks back
                        println("PV de ${personnage.nom}: ${personnage.pv}")
                        println("PV de ${ennemi.nom}: ${ennemi.pv}")

                        if (personnage.pv <= 0 || ennemi.pv <= 0) {
                            Personnage.isDead = true
                            if (personnage.pv <=0){
                                personnage.pv =0;
                            }
                            else{
                                ennemi.pv=0;
                            }
                            println("Fin de la bataille!")
                        }
                    }
                    2 -> {
                    if (specialTurn < 3) { // Check if special attack can be used
                        personnage.attaqueSpecial(ennemi)
                        ennemi.attaquer(personnage) // Enemy attacks back
                        println("PV de ${personnage.nom}: ${personnage.pv}")
                        println("PV de ${ennemi.nom}: ${ennemi.pv}")

                        // Increment SpecialTurn after using the special attack
                        specialTurn++

                        if ((personnage.pv ?: 0) <= 0 || (ennemi.pv ?: 0) <= 0) {
                            Personnage.isDead = true
                            println("Fin de la bataille!")
                        }
                    } else {
                        println("Vous ne pouvez pas utiliser l'attaque spéciale pour le moment.")
                    }
                }
                3 -> {
                    println("Vous avez choisi de fuir, l'ennemi attaque...")
                    ennemi.attaquer(personnage)
                    println("PV de ${personnage.nom}: ${personnage.pv}")
                    println("PV de ${ennemi.nom}: ${ennemi.pv}")

                    if (personnage.pv <= 0 || ennemi.pv <= 0) {
                        Personnage.isDead = true
                        println("Fin de la bataille!")
                    }
                    else{
                        println("vous avez fuit!!")
                    }
                }
                else -> println("Action invalide.")
            }

            turn = if (turn == 1) 0 else 1
        }
    } else {
        println("Choix invalide.")
    }
}
