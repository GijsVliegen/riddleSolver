import java.util.HashSet;
import java.util.Set;

public class player {
    public char name;
    player lookedAt[] = new player[16];
    public int index;
    public int diff;
    public int caseNr = -1;
    public boolean visited = false;

    public player(char name){
        this.name = name;
        clear();
    }
    public void lookedAtAdd(player a){
        /*if(checkNewPlayer(a)) {
            lookedAt[index++] = a;
        }*/
        lookedAt[index++] = a;
        diffPeople();
    }
    public void clear(){
        index = 0;
        for(int i = 0; i < index; i ++){
            lookedAt[i] = null;
        }
    }

    public boolean checkNewPlayer(player a){
        for (int i = 0; i < index; i++){
            if(lookedAt[i] == a){
                return true; //gewoon er dubbel inzetten voorlopig
            }
        }
        return true;
    }

    private void searchRemove(player a){
        boolean removed = false;
        int newIndex = index;
        while(!removed){
            if(lookedAt[--newIndex] == a) {
                adjust(newIndex);
                removed = true;
            }
            if (newIndex < 0){
                System.out.println("er is iets grondig fout gebeurd");
                break;
            }
        }
        diffPeople();
    }
    public void removeOne(){
        lookedAt[index-1].searchRemove(this);
        lookedAt[--index] = null;
    }
    public void adjust(int a){
        for (int i = a; i < index; i++){
            lookedAt[a] = lookedAt[a+1];
        }
        lookedAt[--index] = null;
    }

    public void diffPeople(){
        Set<String> hash_Set = new HashSet<>();
        for(int i = 0; i < index; i++){
            hash_Set.add(String.valueOf(lookedAt[i].name));
        }
        diff = hash_Set.size();
        if (visited == false){
            caseNr = diff;
        }
    }

    public void printLookedAt(){
        String s = "voor player " + String.valueOf(name) + "(" + String.valueOf(caseNr) + ") : " + String.valueOf(lookedAt[0].name);
        for (int i = 1; i < index; i++){
            s += ", " + String.valueOf(lookedAt[i].name);
        }
        System.out.println(s);
    }

    public static void printTotal(player list[]){
        for (int i = 0; i < 8; i++){
            if (list[i].index == 0){
                String s = "voor player " + String.valueOf(list[i].name) + "(" + String.valueOf(list[i].caseNr) + ") : leeg ";
                System.out.println(s);
            }
            else{
                list[i].printLookedAt();
            }
        }
    }
}
