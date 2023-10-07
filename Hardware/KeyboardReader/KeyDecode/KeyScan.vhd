LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY KeyScan is
PORT( RST : in std_logic;
		DATA_IN : in std_logic_vector(3 downto 0);
		Kscan : in STD_LOGIC;
		K : out std_logic_vector(3 downto 0);
		Kpress: out std_logic;
		Osc: in std_logic;
		DecE: out std_logic_vector(2 downto 0)
		);	
end KeyScan;

ARCHITECTURE logicKeyScan OF KeyScan is


		
COMPONENT counterUp2
PORT( RST : in STD_LOGIC;
		CE : in std_logic;
		CLK : IN STD_LOGIC;
		Q : out std_logic_vector(1 downto 0)
		);
		
end component; 	

component Decoder 
	 port(S: in STD_LOGIC_VECTOR(1 downto 0);
      I : out STD_LOGIC_vector(3 downto 0)
   );
end component;

component ScanReg 
	 port( I : in std_logic_vector(1 downto 0);
		EN : in STD_LOGIC;
		RST : in std_logic;
		CLK : IN STD_LOGIC;
		Q : out std_logic_vector(1 downto 0)
   );
end component;

component Penc4x2
	port(I: in std_logic_vector(3 downto 0);
      O : out STD_LOGIC_VECTOR(1 DOWNTO 0);
		GS: out STD_LOGIC
   );
end component;

signal scont, spenc, sreg: std_logic_vector(1 downto 0);
signal DecS, DATA_NOT: std_logic_vector(3 downto 0);

Begin

	Cont : counterUp2 port map (
				RST => RST,
				CE => Kscan,
				CLK => Osc,
				Q => scont
			);
	DEC : Decoder port map (
				S => scont,
				I => DecS
	);
	
	PENC : Penc4x2 port map (
				I => DATA_NOT,
				O => spenc,
				GS => Kpress
	);
	
	REG : ScanReg port map (
				I => Spenc,
				EN => '1',
				RST => RST,
				CLK => Kscan,
				Q => sreg
	);
	
	DATA_NOT <= not DATA_IN;
	DecE <= not DecS(2) & not DecS(1) & not DecS(0);
	K <= scont & sreg;
	
end logicKeyScan;