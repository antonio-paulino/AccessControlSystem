LIBRARY ieee;
USE ieee.std_logic_1164.all;




ENTITY KeyBoardReader is
PORT(	RST 	: in std_logic;
		Osc		:in std_logic;
		ACK		: IN std_logic;
		Lines 	: in std_logic_vector(3 downto 0);
		Q 		: out std_logic_vector(3 downto 0);
		Columns	: out std_logic_vector(2 downto 0);
		Dval	: out std_logic
		);	
end KeyBoardReader;

ARCHITECTURE logicKeyBoardReader OF KeyBoardReader is

signal sKey, sQ : std_logic_vector(3 downto 0);
signal sKval, sWreg, sObfree, sDAC : std_logic;

component KeyDecode is
PORT( 	RST		: IN std_logic;
		DATA_IN : in std_logic_vector(3 downto 0);
		K 		: out std_logic_vector(3 downto 0);
		Kval	: out std_logic;
		Osc		: in std_logic;
		DecE	: out std_logic_vector(2 downto 0);
		Kack	: in std_logic;
		Skpress	: out std_logic;
		Skscan	: out std_logic
		);	
end component;		 
		 
component RingBuffer is
   port(
		D	: in std_logic_vector(3 downto 0);
		clk : in std_logic;
		RST	: in std_logic;
		DAV	: in std_logic;
		CTS	: in std_logic;
		DAC : out std_logic;
		Wreg: out std_logic;
		Q 	: out std_logic_vector(3 downto 0)
   );
end component;

component OutputBuffer is
   port(
		D	: in std_logic_vector(3 downto 0);
		clk : in std_logic;
		RST	: in std_logic;
		Load: in std_logic;
		ACK	: in std_logic;
		Obfree : out std_logic;
		Dval: out std_logic;
		Q 	: out std_logic_vector(3 downto 0)
   );
end component;



Begin 
		Decode: KeyDecode port map(
			RST 	=>  RST,
			DATA_IN => Lines,
			K 		=> sKey,
			DecE 	=> Columns,
			Kval	=> sKval,
			Kack 	=> sDAC,
			Osc 	=> Osc
		);
		
		RingBuff : RingBuffer port map(
			D 	=> sKey,
			clk => osc,
			RST => RST,
			DAV => sKval,
			CTS => sObfree,
			DAC => sDAC,
			Wreg => sWreg,
			Q 	=> sQ
		);
		
		OutBuff : OutputBuffer port map(
			D => sQ,
			clk => osc,
			RST => RST,
			Load => sWreg,
			ACK => ACK,
			Obfree => sObfree,
			Dval => Dval,
			Q => Q
		);
	end logicKeyBoardReader;
		
