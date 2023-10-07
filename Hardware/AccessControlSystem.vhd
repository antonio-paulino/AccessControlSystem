LIBRARY ieee;
USE ieee.std_logic_1164.all;




ENTITY AccessControlSystem is
PORT(
		Osc	  	: in std_logic;
		Lines 	: in std_logic_vector(3 downto 0);
		M 	  		: in std_logic;
		Columns	: out std_logic_vector(2 downto 0);
		RST 		: in std_logic;
		SSQ		: out std_logic_vector (3 downto 0);	--debug
		SSDval 	: out std_logic;								--debug
		Data_LCD	: out  std_logic_vector (7 downto 4);	
		DATA_LCD2: out  std_logic_vector (7 downto 4);	--debug
		RS_LCD	: out std_logic;
		EN_LCD	: out std_logic;								
		LCDSEL 	: out std_logic;								--debug
		Dout 		: out std_logic_vector(4 downto 0); 	--debug
		busy 		: out std_logic;  							--debug
		Onoff 	: out std_logic; 								--debug
		SDX		: out std_logic;   							--debug
		Pdetect	: in std_logic;
		HEX0 		: out std_logic_vector(7 downto 0);	
		HEX1 		: out std_logic_vector(7 downto 0);	
		HEX2 		: out std_logic_vector(7 downto 0);	
		HEX3 		: out std_logic_vector(7 downto 0);	
		HEX4 		: out std_logic_vector(7 downto 0);	
		HEX5 		: out std_logic_vector(7 downto 0)
		);	
end AccessControlSystem;

ARCHITECTURE logicAccessControlSystem OF AccessControlSystem is

	component UsbPort IS 
		PORT
		(
			inputPort:  IN  STD_LOGIC_VECTOR(7 DOWNTO 0);
			outputPort :  OUT  STD_LOGIC_VECTOR(7 DOWNTO 0)
		);
	END component;

	component door_mecanism IS
	PORT(	MCLK 			: in std_logic;
			RST			: in std_logic;
			onOff			: in std_logic;
			openClose	: in std_logic;
			v				: in std_logic_vector(3 downto 0);
			Pswitch		: in std_logic;
			Sopen			: out std_logic;
			Sclose		: out std_logic;
			Pdetector	: out std_logic;
			HEX0			: out std_logic_vector(7 downto 0);
			HEX1			: out std_logic_vector(7 downto 0);
			HEX2			: out std_logic_vector(7 downto 0);
			HEX3			: out std_logic_vector(7 downto 0);
			HEX4			: out std_logic_vector(7 downto 0);
			HEX5			: out std_logic_vector(7 downto 0)
			);
	END component;


	component KeyBoardReader is
	PORT( Osc	:in std_logic;
			ACK	: IN std_logic;
			Lines : in std_logic_vector(3 downto 0);
			Q 		: out std_logic_vector(3 downto 0);
			Columns: out std_logic_vector(2 downto 0);
			Dval	: out std_logic
	);	
	end component;

	component SLCDC is
	PORT( LCDsel : in STD_LOGIC;
			SCLK 	 : in STD_LOGIC;
			SDX 	 : in std_logic;
			clk	 : IN std_logic;
			D 	 	 : out std_logic_vector(4 downto 0);
			E 	 	 : out STD_LOGIC;
			RST 	 : in std_logic
	);	
	end component;

	component SDC is 
	PORT (
			SDCsel : in STD_LOGIC;
			SCLK 	 : in STD_LOGIC;
			SDX 	 : in std_logic;
			clk	 : IN std_logic;
			FCclose: in std_logic;
			FCopen : in std_logic;
			Pdetect: in std_logic;
			OnOff  : out STD_LOGIC;
			Dout   : out std_logic_vector (4 downto 0);
			busy   : out std_logic;
			RST    : in std_logic
	);
	end component;

			signal SKack, SDval, sLCDsel, sCLK, sSDX, sDCsel, sbusy, sOnOff, SSoPEN, sSclose, sPdetect : std_logic;
			signal SQ : std_logic_vector(3 downto 0);
			signal sLCD_data, sDout : std_logic_vector(4 downto 0);
	Begin

		KeyReader : KeyBoardReader port map(
			Osc 	=> Osc,
			ACK 	=> SKack,
			Dval 	=> SDval,
			Lines => Lines,
			Q		=> SQ,
			Columns => Columns
		);

		SDCControl : SDC port map(
			SDCsel 	=> sDCsel,
			SCLK 		=> sCLK,
			SDX 		=> sSDX,
			clk 		=> Osc,
			FCclose 	=> ssClose,
			FCopen 	=> ssOpen,
			Pdetect	=> sPdetect,
		   OnOff 	=> sOnOff,
			Dout 		=> sDout,
			busy 		=> sbusy,
			RST 		=> RST
		);

		busy <= sbusy;

		USB: UsbPort port map (
			inputport(0) 	=> SQ(0),
			inputport(1) 	=> SQ(1),
			inputport(2) 	=> SQ(2),
			inputport(3) 	=> SQ(3),
			inputport(4) 	=> SDval,
			inputport(5) 	=> sbusy,
			inputport(6) 	=> '0',
			inputport(7) 	=> M,
			outputport(0) 	=> ssDX,
		   outputport(1) 	=> sLCDSel,
			outputport(2) 	=> sDCsel,
			--outputport(3) => '0',
			--outputport(4) => '0',
			--outputport(5) => '0',
			outputport(6) 	=> sKack,
		   outputport(7)	=> sCLK
		);


		DoorMechanism : door_mecanism port map(
			MCLK 	  => osc,
			RST 	  => rst,
			onOff   => sOnOff,		
			openClose => sDout(0),	
			v		  => sDout(4) & sDout(3) & sDout(2) & sDout(1),
			Pswitch => Pdetect,		
			Sopen	  => sSopen,
			Sclose  => sSclose,
			Pdetector => sPdetect,
			HEX0 	  => HEX0,		
			HEX1 	  => HEX1,
			HEX2 	  => HEX2,
			HEX3 	  => HEX3,			
			HEX4 	  => HEX4,			
			HEX5 	  => HEX5 		
		);

		SLCDControl : SLCDC port map (
			LCDsel 	=> sLCDsel,
			SCLK 	=> sCLK, 
			SDX  	=> sSDX,
			clk 	=> Osc,
			D 		=> sLCD_data,
			E 		=> EN_LCD,
			RST 	=> RST
		);


		Dout 	<= sDout;
		onOff <= sOnOff;
		LCDSEL<= sLCDsel;
		SDX 	<= ssDX;
		RS_LCD<= sLCD_data(0);
		SSDval<= SDval;
		SSQ 	<= SQ;

		Data_LCD(4) <= sLCD_data(1);
		Data_LCD(5) <= sLCD_data(2);
		Data_LCD(6) <= sLCD_data(3);
		Data_LCD(7) <= sLCD_data(4);

		DATA_LCD2 	<= sLCD_data(4) & sLCD_data(3) & sLCD_data(2) & sLCD_data(1);

end logicAccessControlSystem;



