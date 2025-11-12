package it.unibo.deathnote;

import java.util.HashMap;
import java.util.List;
import it.unibo.deathnote.api.DeathNote;

public class DeathnoteImpl implements DeathNote{
    private final List<String> RULES;
    private String lastName;
    private HashMap<String, DeathInfo> people;
    private double DELTA_CAUSE = 40;
    private double DELTA_DETAILS = 6040;

    public DeathnoteImpl(){
        this.RULES = List.of(
        """
        The human whose name is written in this note shall die.
        """,
        """
        This note will not take effect unless the writer has the subject's face in mind when
        writing his/her name. This is to prevent people who share the same name from being
        affected.
        """,
        """
        After writing the cause of death, details of the death should be written in the next 6
        seconds and 40 milliseconds.
        """,
        """
        The human who touches the Death Note can recognize the image and voice of its original
        owner, a god of death, even if the human is not the owner of the note.
        """,
        """
        The person in possession of the Death Note is possessed by a god of death,
        its original owner, until they die.
        """,
        """
        Gods of death, the original owners of the Death Note, do not do, in principle,
        anything which will help or prevent the deaths in the note. A god of death has no
        obligation to completely explain how to use the note or rules which will apply to the
        human who owns it unless asked.
        """,
        """
        A god of death can extend their own life by putting a name on their own note, but
        humans cannot.
        """,
        """
        The human who becomes the owner of the Death Note can, in exchange of half his/her
        remaining life, get the eyeballs of the god of death which will enable him/her to see
        a human's name and remaining life span when looking through them.
        """,
        """
        The conditions for death will not be realized unless it is physically possible for
        that human or it is reasonably assumed to be carried out by that human.
        """,
        """
        One page taken from the Death Note, or even a fragment of the page, contains the full
        effects of the note.
        """,
        """
        The individuals who lose the ownership of the Death Note will also lose their memory
        of the usage of the Death Note. This does not mean that he will lose all the memory
        from the day he owned it to the day he loses possession, but means he will only lose
        the memory involving the Death Note.
        """,
        """
        The number of pages of the Death Note will never run out.
        """,
        """
        It is useless trying to erase names written in the Death Note with erasers or
        white-out.
        """
        );
    }
    private static class DeathInfo { 
        String cause;
        String details;
        long nameWrittenTime;
        long causeWrittenTime;
    }

    @Override
    public String getRule(int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > this.RULES.size()){
            throw new IllegalArgumentException("invalid number");
        }
        return this.RULES.get(ruleNumber+1); //the +1 is made because the 1st rule
                                            //is counted as 0
    }
    @Override
    public void writeName(String name) {
        if (name.equals(null)){
            throw new NullPointerException("name can't be null");
        }else{
            DeathInfo info = new DeathInfo();
            info.nameWrittenTime = System.currentTimeMillis();;
            this.people.put(name, info);
            this.lastName = name;
        }
        
    }
    @Override
    public boolean writeDeathCause(String cause) {
        if (this.lastName.equals(null)){
            throw new NullPointerException("name can't be null");
        }
        if (cause.equals(null) || this.people.isEmpty()) {
            throw new IllegalStateException("there're no names or the cause os null");
        } else {
            DeathInfo info = this.people.get(this.lastName);
            info.causeWrittenTime = System.currentTimeMillis();
            if (info.causeWrittenTime - info.nameWrittenTime <= DELTA_CAUSE){
                info.cause= cause;
                return true;
            }
            return false;
        }
        
    }

    @Override
    public boolean writeDetails(String details) { 
        if (this.lastName.equals(null)){
                throw new NullPointerException("name can't be null");
        }
        if (details.equals(null) || this.people.isEmpty()) {
            throw new IllegalStateException("there're no names or the details are null");
        } else {
            DeathInfo info = this.people.get(this.lastName);
            if (System.currentTimeMillis() - info.causeWrittenTime <= DELTA_DETAILS){
                info.details = details;
                return true;
            }
            return false;
        }
    }
    @Override
    public String getDeathCause(String name) {
        if (!this.isNameWritten(name)){
            throw new IllegalArgumentException("name isn't in the book");
        }else{
            if (this.people.get(name).cause.equals(null)){
                return "heart attack";
            }else{
                return this.people.get(name).cause;
            }
        }
    }

    @Override
    public String getDeathDetails(String name) {
        if (!this.isNameWritten(name)){
            throw new IllegalArgumentException("name isn't in the book");
        }else{
            if (this.people.get(name).cause.equals(null)){
                return "";
            }else{
                return this.people.get(name).details;
            }
        }
    }

    
    @Override
    public boolean isNameWritten(String name) {
        if(this.people.isEmpty()){
            return false;
        }
        if(this.people.keySet().contains(name)){
            return true;
        }
        return false;
    }

    
}
