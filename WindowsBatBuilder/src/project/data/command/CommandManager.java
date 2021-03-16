package project.data.command;

import java.io.File;
import java.util.ArrayList;

public class CommandManager extends Command
{
	public enum CommandType
	{
		NONE, USER, GROUP, DISK
	}
	
	static ArrayList<Command> userCommands = new ArrayList<Command>();
	static ArrayList<Command> groupCommands = new ArrayList<Command>();
	static ArrayList<Command> diskCommands = new ArrayList<Command>();
	
	public CommandManager()
	{
		loadCommands();
	}
	
	public static void loadCommands()
	{
		loadUserCommands();
		loadGroupCommands();
		loadDiskCommands();
	}
	
	private static void loadUserCommands()
	{
		String insertKey = "insertname";
		String paramKey1 = "param1";
		String paramKey2 = "param2";
		String paramKey3 = "param3";
		String paramKey4 = "param4";
		
		UserCommand command1 = new UserCommand();
		command1.tooltip = "Afficher tout les utilisateurs";
		command1.keyword = "afficher tout les utilisateurs display";
		command1.command.add("net user");
		command1.hasInsertKey = false;
		userCommands.add(command1);
		
		UserCommand command2 = new UserCommand();
		command2.tooltip = "Afficher les informations du compte";
		command2.keyword = "afficher les informations du compte display";
		command2.command.add("net user \"");
		command2.command.add("\"");
		command2.command.add(insertKey);
		userCommands.add(command2);
		
		UserCommand command3 = new UserCommand();
		command3.tooltip = "Changer/definir le mot de passe du compte";
		command3.keyword = "changer definir le mot passe compte";
		command3.command.add("net user \"");
		command3.command.add(insertKey);
		command3.command.add("\" ");
		command3.command.add(paramKey1);
		command3.paramTooltip1 = "Nouveau mot de passe du compte";
		command3.numberOfParam = 1;
		userCommands.add(command3);
		
		UserCommand command4 = new UserCommand();
		command4.deleteUser = true;
		command4.tooltip = "Supprimer le compte";
		command4.keyword = "supprimer le compte /delete";
		command4.command.add("net user \"");
		command4.command.add(insertKey);
		command4.command.add("\" /DELETE");
		userCommands.add(command4);
		
		UserCommand command5 = new UserCommand();
		command5.tooltip = "Desactiver le compte";
		command5.keyword = "desactiver le compte /active";
		command5.command.add("net user \""); 
		command5.command.add(insertKey);
		command5.command.add("\" /ACTIVE:NO");
		userCommands.add(command5);
		
		UserCommand command6 = new UserCommand();
		command6.tooltip = "Activer le compte";
		command6.keyword = "activer le compte /active";
		command6.command.add("net user \""); 
		command6.command.add(insertKey);
		command6.command.add("\" /ACTIVE:YES");
		userCommands.add(command6);
		
		UserCommand command7 = new UserCommand();
		command7.tooltip = "Fixer une date d'expiration du compte";
		command7.keyword = "fixer une date d'expiration du compte /expires";
		command7.command.add("net user \"");
		command7.command.add(insertKey);
		command7.command.add("\" /EXPIRES:");
		command7.command.add(paramKey1);
		command7.command.add("/");
		command7.command.add(paramKey2);
		command7.command.add("/");
		command7.command.add(paramKey3);
		command7.paramTooltip1 = "Entrer l'annee d'expiration(Eg:2021):";
		command7.paramTooltip2 = "Entrer le mois d'expiration(Eg:Mars->03):";
		command7.paramTooltip3 = "Entrer la date d'expiration(Eg:30):";
		command7.numberOfParam = 3;
		userCommands.add(command7);
		
		UserCommand command8 = new UserCommand();
		command8.tooltip = "Limiter le temps d'utilisation du compte";
		command8.keyword = "limiter le temps utilisation du compte heure date restraindre /times";
		command8.command.add("net user \"");
		command8.command.add(insertKey);
		command8.command.add("\" /TIMES:");
		command8.command.add(paramKey1);
		command8.command.add("-");
		command8.command.add(paramKey2);
		command8.command.add(",");
		command8.command.add(paramKey3);
		command8.command.add("-");
		command8.command.add(paramKey4);
		command8.paramTooltip1 = "Premier jour 	(Ex:lundi):";
		command8.paramTooltip2 = "Deuxieme jour	(Ex: vendredi):";
		command8.paramTooltip3 = "Entrer heure de debut + am ou pm(Ex: 9am):";
		command8.paramTooltip4 = "Entrer heure de fin + am ou pm(Ex: 7pm):";
		command8.numberOfParam = 4;
		userCommands.add(command8);
		
		UserCommand command9 = new UserCommand();
		command9.tooltip = "Ajouter un commentaire au compte";
		command9.keyword = "ajouter un commentaire au compte /comment";
		command9.command.add("net user \"");
		command9.command.add(insertKey);
		command9.command.add("\" /COMMENT:\"");
		command9.command.add(paramKey1);
		command9.command.add("\"");
		command9.paramTooltip1 = "Commentaire a ajouter au compte:";
		command9.numberOfParam = 1;
		userCommands.add(command9);
		
		UserCommand command10 = new UserCommand();
		command10.tooltip = "Definir le nom d'utilisateur complet";
		command10.keyword = "definir le nom d'utitilisateur complet enregistrer changer /fullname";
		command10.command.add("net user \"");
		command10.command.add(insertKey);
		command10.command.add("\" /FULLNAME:\"");
		command10.command.add(paramKey1);
		command10.command.add("\"");
		command10.paramTooltip1 = "Entrer le nom complet de l'utilisateur:";
		command10.numberOfParam = 1;
		userCommands.add(command10);
		
		UserCommand command11 = new UserCommand();
		command11.tooltip = "Utilisateur doit changer mot de passe a la prochaine session";
		command11.keyword = "utilisateur doit changer mot de passe a la prochaine session /logonpasswordchg";
		command11.command.add("net user \"");
		command11.command.add(insertKey);
		command11.command.add("\" /LOGONPASSWORDCHG:YES");
		userCommands.add(command11);
		
		//wmic useraccount where "Name='pcunlocker'" set PasswordExpires=false 
		UserCommand command12 = new UserCommand();
		command12.tooltip = "Le mot de passe de l'utilisateur n'expire jamais";
		command12.keyword = "le mot passe de l'utilisateur n'expire jamais wmic useraccount";
		command12.command.add("wmic useraccount where \"Name='");
		command12.command.add(insertKey);
		command12.command.add("'\" set PasswordExpires=false");
		userCommands.add(command12);
		
		//wmic useraccount where "Name='pcunlocker'" set PasswordExpires=true
		UserCommand command13 = new UserCommand();
		command13.tooltip = "Le mot de passe de l'utilisateur doit expirer";
		command13.keyword = "le mot de passe de l'utilisateur doit expirer wmic PasswordExpires";
		command13.command.add("wmic useraccount where \"Name='");
		command13.command.add(insertKey);
		command13.command.add("'\" set PasswordExpires=true");
		userCommands.add(command13);
		
		UserCommand command14 = new UserCommand();
		command14.tooltip = "Definir la longueur minimal du mot de passe de tout les utilisateurs";
		command14.keyword = "definir le longueur minimal du mot de passe tout les utilisateurs /minpwlen:";
		command14.command.add("net accounts /MINPWLEN:");
		command14.command.add(paramKey1);
		command14.paramTooltip1 = "Entrer la longueure minimal des mots de passe";
		command14.numberOfParam = 1;
		command14.hasInsertKey = false;
		userCommands.add(command14);
		
		UserCommand command15 = new UserCommand();
		command15.tooltip = "L'utilisateur ne peut pas changer son mot de passe";
		command15.keyword = "l'utilisateur ne peut pas changer son mot de passe /passwordchg:no";
		command15.command.add("net user ");
		command15.command.add(insertKey);
		command15.command.add(" /Passwordchg:No");
		userCommands.add(command15);
		
		UserCommand command16 = new UserCommand();
		command16.tooltip = "L'utilisateur a le droit de changer son mot de passe";
		command16.keyword = "L'utilisateur a le droit de changer son mot de passe /passwordchg:yes";
		command16.command.add("net user ");
		command16.command.add(insertKey);
		command16.command.add(" /Passwordchg:Yes");
		userCommands.add(command16);
		
		UserCommand command17 = new UserCommand();
		command17.tooltip = "Definir la duree maximal du mot de passe";
		command17.keyword = "definir la duree maximal du mot de passe /maxpwage:";
		command17.command.add("net accounts /MAXPWAGE:");
		command17.command.add(paramKey1);
		command17.paramTooltip1 = "Entrer la duree maximal du mot de passe:";
		command17.numberOfParam = 1;
		command17.hasInsertKey = false;
		userCommands.add(command17);
		
		UserCommand command18 = new UserCommand();
		command18.tooltip = "Definir le nombre de mots de passe anterieurs a conserver";
		command18.keyword = "definir le nombre de mots de passe anterieurs a conserver /UNIQUEPW:";
		command18.command.add("net accounts /UNIQUEPW:");
		command18.command.add(paramKey1);
		command18.paramTooltip1 = "Entrer le nombre de mots de passe a conserver:";
		command18.numberOfParam = 1;
		command18.hasInsertKey = false;
		userCommands.add(command18);
		
		UserCommand command19 = new UserCommand();
		command19.tooltip = "Afficher les parametres globaux de comptes";
		command19.keyword = "Afficher les parametres globaux de comptes";
		command19.command.add("net accounts");
		command19.hasInsertKey = false;
		userCommands.add(command19);
	}
	
	private static void loadGroupCommands()
	{
		String insertKey = "insertname";
		String param1 = "param1";
		
		UserCommand command1 = new UserCommand();
		command1.tooltip = "Afficher groupes";
		command1.keyword = "afficher groupes";
		command1.command.add("net localgroup");
		command1.hasInsertKey = false;
		groupCommands.add(command1);
		
		UserCommand command2 = new UserCommand();
		command2.addUserToGroup = true;
		command2.tooltip = "Ajouter un utilisateur au groupe";
		command2.keyword = "ajouter un utilisateur au groupe /add";
		command2.command.add("net localgroup \"");
		command2.command.add(insertKey);
		command2.command.add("\" ");
		command2.command.add(param1);
		command2.command.add(" /ADD");
		command2.numberOfParam = 1;
		command2.paramTooltip1 = "Entrer le nom de l'utilisateur a ajouter au groupe";
		groupCommands.add(command2);
		
		UserCommand command3 = new UserCommand();
		command3.deleteUserFromGroup = true;
		command3.tooltip = "Enlever un utilisateur du groupe";
		command3.keyword = "enlever un utilisateur du groupe supprimer /delete";
		command3.command.add("net localgroup \"");
		command3.command.add(insertKey);
		command3.command.add("\" ");
		command3.command.add(param1);
		command3.command.add(" /DELETE");
		command3.numberOfParam = 1;
		command3.paramTooltip1 = "Entrer le nom de l'utilisateur a retirer du groupe";
		groupCommands.add(command3);
		
		UserCommand command4 = new UserCommand();
		command4.deleteGroup = true;
		command4.tooltip = "Supprimer un groupe";
		command4.keyword = "supprimer un groupe /delete";
		command4.command.add("net localgroup \"");
		command4.command.add(insertKey);
		command4.command.add("\" /DELETE");
		groupCommands.add(command4);
	}
	
	private static void loadDiskCommands()
	{
		String insertKey = "insertname";
		String paramKey1 = "param1";
		String paramKey2 = "param2";
		String paramKey3 = "param3";
		String paramKey4 = "param4";
		
		UserCommand command1 = new UserCommand();
		command1.tooltip = "Creer un nouveau volume";
		command1.keyword = "creer un nouveau volume";
		command1.command.add("select disk ");
		command1.command.add(insertKey);
		command1.command.add("\ncreate volume simple size=");
		command1.command.add(paramKey1);
		command1.command.add("\nformat fs=");
		command1.command.add(paramKey2);
		command1.command.add(" label=\"");
		command1.command.add(paramKey3);
		command1.command.add("\" quick");
		command1.command.add("\nassign letter =\"");
		command1.command.add(paramKey4);
		command1.command.add("\"");
		command1.numberOfParam = 4;
		command1.paramTooltip1 = "Entrer la taille du volume:";
		command1.paramTooltip2 = "Entrer le format du volume(NTFS/FAT32):";
		command1.paramTooltip3 = "Entrer le label du volume:";
		command1.paramTooltip4 = "Entrer la lettre a assigner au volume:";
		diskCommands.add(command1);
		
		UserCommand command2 = new UserCommand();
		command2.tooltip = "Etendre un volume";
		command2.keyword = "etendre un volume extend";
		command2.command.add("select disk ");
		command2.command.add(insertKey);
		command2.command.add("\nselect volume ");
		command2.command.add(paramKey1);
		command2.command.add("\nextend size=");
		command2.command.add(paramKey2);
		command2.command.add(" disk=");
		command2.command.add(paramKey3);
		command2.numberOfParam = 3;
		command2.paramTooltip1 = "Entrer la lettre du volume a etendre:";
		command2.paramTooltip2 = "Entrer la taille de l'extension du volume";
		command2.paramTooltip3 = "Entrer le disque sur lequel on doit etendre le volume:(CHIFFRE SEULEMENT Eg:1)";
		diskCommands.add(command2);
		
		UserCommand command3 = new UserCommand();
		command3.tooltip = "Reduire un volume";
		command3.keyword = "reduire un volume shrink";
		command3.command.add("select disk ");
		command3.command.add(insertKey);
		command3.command.add("\nselect volume ");
		command3.command.add(paramKey1);
		command3.command.add("\nshrink desired=");
		command3.command.add(paramKey2);
		command3.numberOfParam = 2;
		command3.paramTooltip1 = "Entrer la lettre du volume a reduire:";
		command3.paramTooltip2 = "Entrer la taille de la reduction du volume:";
		diskCommands.add(command3);
		
		UserCommand command4 = new UserCommand();
		command4.tooltip = "Supprimer un volume";
		command4.keyword = "supprimer un volume delete";
		command4.command.add("select disk ");
		command4.command.add(insertKey);
		command4.command.add("\nselect volume ");
		command4.command.add(paramKey1);
		command4.command.add("\ndelete volume");
		command4.numberOfParam = 1;
		command4.paramTooltip1 = "Entrer la lettre du volume a supprimer:";
		diskCommands.add(command4);
		
		UserCommand command5 = new UserCommand();
		command5.tooltip = "Nettoyer un disque(Supprime tout les volumes)";
		command5.keyword = "nettoyer un disque clean";
		command5.command.add("select disk ");
		command5.command.add(insertKey);
		command5.command.add("\nclean ");
		command5.command.add("\nconvert gpt");
		command5.command.add("\nconvert dynamic");
		diskCommands.add(command5);
		
		UserCommand command6 = new UserCommand();
		command6.tooltip = "Convertir un volume FAT32 en NTFS";
		command6.keyword = "convertir un volume fat32 en ntfs";
		command6.command.add("convert ");
		command6.command.add(paramKey1);
		command6.command.add(": /fs :ntfs");
		command4.numberOfParam = 1;
		command4.paramTooltip1 = "Entrer la lettre du volume a convertir:";
		diskCommands.add(command6);
	}
	
	public static String getAddUserCommand(String userName, String password)
	{
		if(password.length() == 0)
		{
			return "rem Ajout d'utilisateur: "+userName+"\n"+"net user \""+userName+"\" /ADD";
		}
		else
		{
			return "rem Ajout d'utilisateur: "+userName+" , Mot de passe: "+password+"\n"+"net user \""+userName+"\" "+password+" /ADD";	
		}
	}
	
	public static String getAddGroupCommand(String groupName)
	{
		return "rem Ajout du groupe: "+groupName+"\n"+"net localgroup \""+groupName+"\" /ADD";
	}
	
	public ArrayList<Command> getCommands(CommandType commandType) 
	{
		switch(commandType)
		{
		case USER:
			return userCommands;
		case GROUP:
			return groupCommands;
		case DISK:
			return diskCommands;
		default:
			return null;
		}
	}
	
	public static ArrayList<Command> findCommandByKeyword(CommandType commandType, String keyword)
	{
		ArrayList<Command> foundCommands = new ArrayList<Command>();
		ArrayList<Command> commands = new ArrayList<Command>();
		keyword = keyword.toLowerCase();
		
		switch(commandType)
		{
		case USER:
			commands = userCommands;
			break;
		case GROUP:
			commands = groupCommands;
			break;
		case DISK:
			commands = diskCommands;
			break;
		default:
			
			break;
		}
		
		for(Command command : commands)
		{
			if(command.keyword.contains(keyword))
			{
				foundCommands.add(command);
			}
		}
		
		return foundCommands;
	}
	
	public static boolean isCommandInputUnique(CommandType commandType, String input)
	{
		ArrayList<Command> commands = new ArrayList<Command>();
		
		switch(commandType)
		{
		case USER:
			commands = userCommands;
			break;
		case GROUP:
			commands = groupCommands;
			break;
		case DISK:
			commands = diskCommands;
			break;
		default:
			
			break;
		}
		
		for(Command command : commands)
		{
			if(command.tooltip.equalsIgnoreCase(input))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static String getDiskBatFileCmd(File batFile)
	{
		String command;
		
		command = "@echo off\r\n"
				+ "\r\n"
				+ ":: BatchGotAdmin\r\n"
				+ ":-------------------------------------\r\n"
				+ "REM  --> Check for permissions\r\n"
				+ ">nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"\r\n"
				+ "\r\n"
				+ "REM --> If error flag set, we do not have admin.\r\n"
				+ "if '%errorlevel%' NEQ '0' (\r\n"
				+ "    echo Requesting administrative privileges...\r\n"
				+ "    goto UACPrompt\r\n"
				+ ") else ( goto gotAdmin )\r\n"
				+ "\r\n"
				+ ":UACPrompt\r\n"
				+ "    echo Set UAC = CreateObject^(\"Shell.Application\"^) > \"%temp%\\getadmin.vbs\"\r\n"
				+ "    echo UAC.ShellExecute \"%~s0\", \"\", \"\", \"runas\", 1 >> \"%temp%\\getadmin.vbs\"\r\n"
				+ "\r\n"
				+ "    \"%temp%\\getadmin.vbs\"\r\n"
				+ "    exit /B\r\n"
				+ "\r\n"
				+ ":gotAdmin\r\n"
				+ "    if exist \"%temp%\\getadmin.vbs\" ( del \"%temp%\\getadmin.vbs\" )\r\n"
				+ "    pushd \"%CD%\"\r\n"
				+ "    CD /D \"%~dp0\"\r\n"
				+ ":--------------------------------------\r\n"
				+ "\r\n"
				+ "diskpart /s "+batFile.getAbsolutePath()+"\r\n"
				+ "pause";
		
		return command;
	}
}
