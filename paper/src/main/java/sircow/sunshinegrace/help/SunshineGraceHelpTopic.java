package sircow.sunshinegrace.help;

import org.bukkit.command.Command;
import org.bukkit.help.GenericCommandHelpTopic;

public class SunshineGraceHelpTopic extends GenericCommandHelpTopic {
    public SunshineGraceHelpTopic(Command command) {
        super(command);
        this.name = command.getLabel();
    }
}
