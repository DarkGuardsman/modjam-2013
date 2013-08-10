package dark.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandToggleHive extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "Hive";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] astring)
    {
        if (sender != null)
        {

        }

    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "hive.command.name";
    }

}
