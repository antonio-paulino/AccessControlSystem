LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY KeyDecode is
PORT( RST: IN std_logic;
		DATA_IN : in std_logic_vector(3 downto 0);
		K : out std_logic_vector(3 downto 0);
		Kval: out std_logic;
		Osc: in std_logic;
		DecE: out std_logic_vector(2 downto 0);
		Kack: in std_logic;
		Skpress: out std_logic;
		Skscan: out std_logic
		);	
end KeyDecode;

ARCHITECTURE logicKeyDecode OF KeyDecode is

component KeyScan is
PORT( RST : in std_logic;
		DATA_IN : in std_logic_vector(3 downto 0);
		Kscan : in STD_LOGIC;
		K : out std_logic_vector(3 downto 0);
		Kpress: out std_logic;
		Osc: in std_logic;
		DecE: out std_logic_vector(2 downto 0)
		);	
end component;	

component KeyControl is
	port (
		RST : in std_logic; 
		clk : in std_logic;
		Kpress : in std_logic;
		Kscan : out std_logic;
		Kack : in std_logic;
		Kval : out std_logic
	);
end component;

component CLKDIV is
generic(div: natural := 50000000);
port ( clk_in: in std_logic;
		 clk_out: out std_logic);
end component;


signal Sscan, press, sclk: std_logic;
Begin 

		
		divisor: CLKDIV generic map(500000) port map(
			clk_in => Osc,
			clk_out => sclk
		);
		
		
		scan: KeyScan port map(
			RST => RST,
			DATA_IN => DATA_IN,
			Kscan => Sscan,
			K => K,
			Osc => sclk,
			DecE => DecE,
			Kpress => press
		);	
		
		Controlo: KeyControl port map(
			RST => RST,
			clk => sclk,
			Kpress => press,
			Kscan => Sscan,
			Kack => Kack,
			Kval => Kval
		
		);
		
		Skpress <= press;
		Skscan <= Sscan;
end logicKeyDecode;