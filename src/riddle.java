import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
Het raadsel:

Omdat je je mondmasker ondersteboven hebt gedragen, word je in de bak gegooid. In de gevangenis is er
een speciaal deel waarbij er één centrale ruimte is met acht aanliggende kamers. Er is geen enkele
manier om deze acht kamers te onderscheiden. Bovendien kan je vanuit de kamers niet in de centrale
ruimte kijken of omgekeerd.
Er zit wel een deur in elke kamer die naar de centrale ruimte leidt. De kamers zijn genummerd van 1-8,
maar dit kan je niet zien in de centrale ruimte. Zo meteen word jij samen met zeven andere gevangenen
elk in één kamer gezet. Het doel is simpel: een van jullie moet achterhalen waar alle anderen zitten.
Als je daarin slaagt, word je vrijgelaten. Om dit te kunnen doen, verloopt het spelletje als volgt:
op een bepaald moment kiest een cipier een willekeurige kamer waarvan de gevangene naar de centrale
kamer mag komen. Deze gevangene mag eerst een deur openen en dan toedoen naar keuze (waardoor de
gevangenen elkaar zien) en dan nog een tweede deur openen en toedoen naar keuze. Zodra dit gebeurd is,
keert de gevangene terug naar zijn kamer. Dan mag de volgende gevangene, in de kamer links van de eerste,
hetzelfde doen en dit totdat elke gevangene geweest is. Let op: elke kamer is geluidsdicht en je zal ook
niet weten wie al voor je gegaan is. Het enige dat je weet, zodra je als gevangene wordt opgeroepen om
naar de centrale ruimte te gaan, is hoeveel en door wie jouw deur reeds is opengegaan. Jullie mogen op
voorhand een tactiek bespreken, maar daarna is elke vorm van communicatie strikt verboden, ook als jullie
elkaars deur opendoen. De vraag is nu uiteraard: kom met een tactiek zodat je sowieso het spelletje wint.
 */

/*
main tactiek van het oplossen, je kan tellen de hoeveelste kamer van links, vanuit je eigen kamer uit, je opendoet.
Dit programme lost het raadsel op, geeft aan wat de tactiek is (welke kamers van links geteld je moet bezoeken.
Er zijn 607 verschillende oplossingen.
 */
public class riddle {
    int cases[][] = new int[8][2];
    player list[] = new player[8];
    Set<int[][]> allSolutions = new HashSet<>();

    public int[][] kopieCases(){
        int b[][] = new int[8][2];
        for (int i = 0; i < 8; i++){
            b[i][0] = cases[i][0];
            b[i][1] = cases[i][1];
        }
        return b;
    }

    public boolean findPattern(player Player){
        for(int j = 1; j < 7; j++){
            cases[Player.caseNr][0] = j;
            for(int k = j+1; k < 8; k++){
                cases[Player.caseNr][1] = k;
                addVisitors(Player);
                if(nextPlayer(Player)){
                    return true;
                }
                else {
                    removeStep(Player);
                }
            }
        }
        cases[Player.caseNr][0] = 0;
        cases[Player.caseNr][1] = 0;
        System.out.println("weg van player");
        return false;
    }

    public boolean patternExists(player Player){
        addVisitors(Player);
        if(nextPlayer(Player)){
            System.out.println("weg van player");
            return true;
        }
        else{
            System.out.println("weg van player");
            removeStep(Player);
            return false;
        }
    }

    public boolean solution(){
        for (int i = 0; i < 8; i++){
            if(list[i].diff > 6){
                System.out.println("persoon die iedereen ziet = " + list[i].name);
                allSolutions.add(kopieCases());
                return true;
            }
        }
        return false;
    }

    //ook bij de persoon waarheen Player is gaan kijken, moet je dingen verwijderen(gebeurt nu in player class)
    public void removeStep(player Player){
        Player.removeOne();
        Player.removeOne();
        Player.diffPeople();
        //player.printTotal(list, player.name - 96);
    }

    public void addVisitors(player Player){
        //                      get nr of player (a = 0), get both cases         mod(8)
        Player.lookedAtAdd(list[((int)Player.name - 97 + cases[Player.caseNr][0])%8]);
        Player.lookedAtAdd(list[((int)Player.name - 97 + cases[Player.caseNr][1])%8]);
        list[((int)Player.name - 97 + cases[Player.caseNr][0])%8].lookedAtAdd(Player);
        list[((int)Player.name - 97 + cases[Player.caseNr][1])%8].lookedAtAdd(Player);
    }

    /* krijgt een speler (a), zoekt de volgende speler (b) als die bestaat,
        en als die bestaat, neemt het de case van b, of zoekt het een nieuwe case voor b;
        en returned de oplossingsvalue naar a
     */
    public boolean nextPlayer(player Player){
        player.printTotal(list);
        /*if(Player.name == 'c'){
            return true;
        }*/
        if(Player.name == 'h'){
            solution();
            System.out.println("weg van player");
            return false;
        }
        player nextP = next(Player);
        System.out.println("volgende player");
        if (cases[nextP.caseNr][0] >= 1){
            nextP.visited = true;
            boolean result = patternExists(nextP);
            nextP.visited = false;
            return result;
        }
        else{
            nextP.visited = true;
            boolean result = findPattern(nextP);
            nextP.visited = false;
            return result;
        }
    }

    public player next(player Player){
        return list[((int)Player.name - 97 + 1)%8];
    }

    public void init(){
        char names[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        for(int i = 0; i < 8; i++){
            list[i] = new player(names[i]);
            cases[i] = new int[]{0, 0};
        }

    }

    public static void main(String[] args){
        riddle riddle = new riddle();
        riddle.init();
        riddle.list[0].visited = true;
        riddle.list[0].caseNr = 0;
        if(riddle.findPattern(riddle.list[0])){
            System.out.println("we hebben een oplossing");
        }
        riddle.printOplossingen();
    }

    int v = 0;

    public void printOplossing(int[][] b){
        System.out.println("oplossing " + (++v));
        for(int i = 0; i < 8; i++){
            String s = "case " + i + ": " + b[i][0] + ", " + b[i][1];
            System.out.println(s);
        }
    }

    public void printOplossingen(){
        allSolutions.forEach(this::printOplossing);
        System.out.println("aantal oplossingen: " + (--v));
    }
}
