package xyz.vsngamer.elevator;

public class Ref {
	
	public static final String MOD_ID = "elevatorid";
	public static final String NAME = "Elevator Mod";
	public static final String VERSION = "1.2.0";
	public static final String ACCPEPTED_VERSIONS = "[1.10.2]";
	
	public static final String CLIENT_PROXY_CLASS = "xyz.vsngamer.elevator.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "xyz.vsngamer.elevator.proxy.ServerProxy";
	
	public static final String GUI_FACTORY = "xyz.vsngamer.elevator.gui.GuiFactory";
	
	public static enum EBlocks {
		
		ELEVATOR("elevator", "BlockElevator"),
		ELEVATOR_BLACK("elevator_black", "BlockElevatorBlack"),
		ELEVATOR_BLUE("elevator_blue", "BlockElevatorBlue"),
		ELEVATOR_BROWN("elevator_brown", "BlockElevatorBrown"),
		ELEVATOR_CYAN("elevator_cyan", "BlockElevatorCyan"),
		ELEVATOR_GRAY("elevator_gray", "BlockElevatorGray"),
		ELEVATOR_GREEN("elevator_green", "BlockElevatorGreen"),
		ELEVATOR_LIGHT_BLUE("elevator_light_blue", "BlockElevatorLightBlue"),
		ELEVATOR_LIME("elevator_lime", "BlockElevatorLime"),
		ELEVATOR_MAGENTA("elevator_magenta", "BlockElevatorMagenta"),
		ELEVATOR_ORANGE("elevator_orange", "BlockElevatorOrange"),
		ELEVATOR_PINK("elevator_pink", "BlockElevatorPink"),
		ELEVATOR_PURPLE("elevator_purple", "BlockElevatorPurple"),
		ELEVATOR_RED("elevator_red", "BlockElevatorRed"),
		ELEVATOR_SILVER("elevator_silver", "BlockElevatorSilver"),
		ELEVATOR_YELLOW("elevator_yellow", "BlockElevatorYellow");
		
		private String unlocalizedName;
		private String registryName;
		
		EBlocks(String unlocalizedName, String registryName){
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		}
		
		public String getUnlocalizedName(){
			return unlocalizedName;
		}
		
		public String getRegistryName(){
			return registryName;
		}
	}
}
