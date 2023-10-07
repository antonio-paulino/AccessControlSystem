LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY counterUp2 is
PORT( 
		RST 	: in STD_LOGIC;
		CE 	: in std_logic;
		CLK 	: IN STD_LOGIC;
		Q 		: out std_logic_vector(1 downto 0)
);	
end counterUp2;

ARCHITECTURE logiccount OF counterUp2 is
		
	COMPONENT register2
	PORT( I	 : in std_logic_vector(1 downto 0);
			EN  : in STD_LOGIC;
			RST : in std_logic;
			CLK : IN STD_LOGIC;
			Q 	 : out std_logic_vector(1 downto 0)
	);	

	end component; 	

	component Adder2 
		 port (
			A 	: in std_logic_vector(1 downto 0);
			B 	: in std_logic_vector(1 downto 0);
			CI : in std_logic;
			S 	: out std_logic_vector(1 downto 0);
			CO : out std_logic
	);
	end component;


	signal SADDER: std_logic_vector(1 downto 0);
	signal SREGISTER: std_logic_vector(1 downto 0);

	BEGIN

	REGISTERL : register2 port map(
		I 		=> SADDER,
		EN 	=> CE,
		CLK 	=> CLK,
		Q 		=> SREGISTER,
		RST 	=> RST
	);

	ADDSUB : Adder2 port map(
		A 	=> SREGISTER,
		B 	=> "01",
		CI => '0',
		S 	=> SADDER
	);
	
	Q <= SREGISTER;

END logiccount;
