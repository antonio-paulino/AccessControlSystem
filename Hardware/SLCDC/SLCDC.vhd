LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY SLCDC is
PORT( LCDsel : in STD_LOGIC;
		SCLK : in STD_LOGIC;
		SDX : in std_logic;
		clk: IN std_logic;
		D : out std_logic_vector(4 downto 0);
		E : out STD_LOGIC;
		RST : in std_logic
		
		);	
end SLCDC;


ARCHITECTURE logic OF SLCDC is


component SerialReceiver is
PORT( 
		SDX : in STD_LOGIC;
		SCLK : in STD_LOGIC;
		SS : in std_logic;
		accept : IN STD_LOGIC;
		clk: IN std_logic;
		D : out std_logic_vector(4 downto 0);
		DXval : out STD_LOGIC;
		RST: in std_logic
		
		
		);	
end component;

component LCD_Dispatcher is
	port (
		clk : in std_logic;
		RST : in std_logic;
		Dval : in std_logic; 
		Din : in std_logic_vector ( 4 downto 0);
		WrL : out std_logic;
		Dout : out std_logic_vector (4 downto 0);
		done : out std_logic
	);
end component;
signal sD: std_logic_vector (4 downto 0);
signal sdone, sval: std_logic;

Begin


Receiver : SerialReceiver port map(

	SS => LCDsel,
	SCLK => SCLK,
	SDX => SDX,
	accept => sdone,
	clk => clk,
	D => sD,
	DXval => sval,
	RST => RST

);


Dispatcher : LCD_DISPATCHER port map (
	clk => clk,
	RST => RST,
	Dval => sval,
	Din => sD,
	WrL => E,
	Dout => D,
	done => sdone
);


end logic;
