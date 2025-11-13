package it.unibo.deathnote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.deathnote.api.DeathNote;

/**
 * Implementation of a Death Note that stores written names and
 * their associated death information in memory.
 *
 * <p>This class is not intended to be subclassed.</p>
 */
public final class DeathnoteImpl implements DeathNote {
    private static final double DELTA_CAUSE = 40;
    private static final double DELTA_DETAILS = 6040;
    private static final String DEFAULTDEATH = "heart attack";
    private static final String NAMENULL = "name can't be null";
    private final List<String> rules; 
    private String lastName;
    private Map<String, DeathInfo> people; //NOPMD it's says that should be final, but it will be modified
    private boolean causeCompleted;
    private boolean detailsCompleted;

    /**
     * Creates a new Death Note instance, initializing it with the
     * predefined set of rules and an empty collection of written names.
     */
    public DeathnoteImpl() {
        this.people = new HashMap<>();
        this.rules = List.of(
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
        white - out.
        """
        );
    }

    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > this.rules.size()) {
            throw new IllegalArgumentException("invalid number");
        }
        return this.rules.get(ruleNumber - 1); //the +1 is made because the 1st rule
        //is counted as 0.
    }

    @Override
    public void writeName(final String name) {
        if (name == null) {
            throw new NullPointerException(NAMENULL); //NOPMD
        } else {
            final DeathInfo info = new DeathInfo();
            info.nameWrittenTime = System.currentTimeMillis();
            this.people.put(name, info);
            this.lastName = name;
            this.causeCompleted = false;
            this.detailsCompleted = false;
        }
    }

    @Override
    public boolean writeDeathCause(final String cause) {
        if (cause == null || this.people.isEmpty()) {
            throw new IllegalStateException("there're no names or the cause os null");
        }
        if (this.lastName == null) {
            throw new NullPointerException(NAMENULL); //NOPMD
        } else {
            final DeathInfo info = this.people.get(this.lastName);
            info.causeWrittenTime = System.currentTimeMillis();
            if (info.causeWrittenTime - info.nameWrittenTime <= DELTA_CAUSE && !this.causeCompleted) {
                if (!"".equals(cause)) { //this is made to avoid null pointer exception
                    info.cause = cause;
                }
                this.causeCompleted = true;
                return true;
            }
            return false;
            }
    }

    @Override
    public boolean writeDetails(final String details) { 
        if (details == null || this.people.isEmpty()) {
            throw new IllegalStateException("there're no names or the details are null");
        }
        if (this.lastName == null) {
                throw new NullPointerException(NAMENULL); //NOPMD
        } else {
            final DeathInfo info = this.people.get(this.lastName);
            if (System.currentTimeMillis() - info.causeWrittenTime <= DELTA_DETAILS && !this.detailsCompleted) {
                info.details = details;
                return true;
            }
            return false;
        }
    }

    @Override
    public String getDeathCause(final String name) {
        if (!this.isNameWritten(name)) {
            throw new IllegalArgumentException("name isn't in the book");
        } else {
            return this.people.get(name).cause;
        }
    }

    @Override
    public String getDeathDetails(final String name) {
        if (!this.isNameWritten(name)) {
            throw new IllegalArgumentException("name isn't in the book");
        } else {
            if (this.people.get(name).cause == null) {
                return "";
            } else {
                return this.people.get(name).details;
            }
        }
    }

    @Override
    public boolean isNameWritten(final String name) {
        return this.people.keySet().contains(name); //!this.people.isEmpty() && 
    }

    /**
     * Returns the total number of rules available in this Death Note.
     *
     * @return the number of rules defined for this note
     */
    public int getNumberOfRules() {
        return this.rules.size();
    }

    /**
     * Returns the default cause of death used when no explicit
     * cause is specified for a written name.
     *
     * @return the default cause of death
     */
    public String getDefaultDeath() {
        return this.DEFAULTDEATH;
    }

    /**
     * Internal data holder for a single entry in the Death Note.
     * It stores the cause and details of death together with the
     * timestamps related to when the name and the cause were written.
     */
    private static final class DeathInfo { 
        private String cause = DEFAULTDEATH;
        private String details = "";
        private long nameWrittenTime;
        private long causeWrittenTime;
    }
}
