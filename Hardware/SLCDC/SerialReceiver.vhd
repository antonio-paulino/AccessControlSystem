LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY SerialReceiver is
PORT( busy: out std_logic;
		SDX : in STD_LOGIC;
		SCLK : in STD_LOGIC;
		SS : in std_logic;
		accept : IN STD_LOGIC;
		clk: IN std_logic;
		D : out std_logic_vector(4 downto 0);
		DXval : out STD_LOGIC;
		RST: in std_logic
		);	
end SerialReceiver;

ARCHITECTURE logic OF SerialReceiver is



component SerialControl is
	port (
		RST : in std_logic; 
		clk : in std_logic;
		enRX : in std_logic; 
		accept : in std_logic;
		eq5 : in std_logic;
		clr : out std_logic;
		wr : out std_logic;
		DXval : out std_logic
	);
end component;


component counterUp3 is
PORT( 
		RST : in STD_LOGIC;
		CE : in std_logic;
		CLK : IN STD_LOGIC;
		Q : out std_logic_vector(2 downto 0)
		);	
end component;

component Shiftregister is
PORT( I : in std_logic;
		EN : in STD_LOGIC;
		RST : in std_logic;
		CLK : IN STD_LOGIC;
		DATA : out std_logic_vector(4 downto 0)
		);	
end component;

signal sclr, swr, seq5, sval: std_logic;
signal scounter : std_logic_vector ( 2 downto 0);

Begin

Control : SerialControl port map(
	RST => RST,
	clk => clk,
	enRX => SS,
	accept => accept,
	eq5 => seq5,
	clr => sclr,
	wr => swr,
	DXval => sval
);

Counter: counterUp3 port map(
	RST => sclr,
	CE => '1',
	CLK => SCLK,
	Q => scounter
);


SRegister : Shiftregister port map(
	I => SDX,
	EN => swr,
	RST => RST,
	CLK => SCLK,
	DATA => D
);

busy <= sval;
DXval <= sval;
seq5 <= scounter(0) and not scounter(1) and scounter(2) and not sclk;
end logic;