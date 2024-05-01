LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY SDC is
PORT( SDCsel	: in STD_LOGIC;
		SCLK 		: in STD_LOGIC;
		SDX		: in std_logic;
		clk		: IN std_logic;
		FCclose	: in std_logic;
		FCopen	: in std_logic;
		Pdetect	: in std_logic;
		OnOff 	: out STD_LOGIC;
		Dout 		: out std_logic_vector (4 downto 0);
		busy 		: out std_logic;
		RST		: in std_logic
);	
end SDC;


ARCHITECTURE logic OF SDC is

	component SerialReceiver is
	PORT( busy	: out std_logic;
			SDX 	: in STD_LOGIC;
			SCLK 	: in STD_LOGIC;
			SS 	: in std_logic;
			accept: IN STD_LOGIC;
			clk	: IN std_logic;
			D 		: out std_logic_vector(4 downto 0);
			DXval : out STD_LOGIC;
			RST	: in std_logic
	);	
	end component;

	component DoorController is
		port (
			clk 	: in std_logic;
			RST 	: in std_logic;
			Din	: in std_logic_vector(4 downto 0);
			Dval 	: in std_logic;
			Sclose: in std_logic;
			Sopen	: in std_logic;
			Psensor: in std_logic;
			OnOff	: out std_logic;
			Done	: out std_logic;
			Dout	: out std_logic_vector(4 downto 0)
	);
	end component;

	signal sD: std_logic_vector (4 downto 0);
	signal sdone, sval : std_logic;

	Begin


	Receiver : SerialReceiver port map(
		busy 	=> busy,
		SS 	=> SDCsel,
		SCLK 	=> SCLK,
		SDX	=> SDX,
		accept => sdone,
		clk 	=> clk,
		D 		=> sD,
		DXval => sval,
		RST 	=> RST
	);

	Controller : DoorController port map(
		clk 	=> clk,
		RST 	=> RST,
		Din 	=> sD,
		Dval 	=>	 sval,
		sclose => FCClose,
		sopen  => FCOpen,
		Psensor=> Pdetect,
		OnOff	 => Onoff,
		Done 	=> sdone,
		Dout 	=> Dout
	);

end logic;
