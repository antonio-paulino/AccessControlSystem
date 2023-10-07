LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity RingBuffer is
   port(
		D		: in std_logic_vector(3 downto 0);
		clk 	: in std_logic;
		RST	: in std_logic;
		DAV	: in std_logic;
		CTS	: in std_logic;
		DAC 	: out std_logic;
		Wreg	: out std_logic;
		Q 		: out std_logic_vector(3 downto 0)
   );
end RingBuffer;

architecture arq_buffer of RingBuffer is
signal sAddr : std_logic_vector(2 downto 0);
signal full, empty, sWr, putget, incget, incput : std_logic;


	component RingBufferControl is
		port (
			clk 	: in std_logic;
			RST 	: in std_logic;
			DAV	: in std_logic;
			CTS 	: in std_logic;
			full	: in std_logic;
			empty	: in std_logic;
			Wr		: out std_logic;
			selPG	: out std_logic;	
			Wreg	: out std_logic;	
			DAC	: out std_logic;	
			incput: out std_logic;	
			incget: out std_logic	
	);
	end component;
	
component MAC is
   port(
		clk 	: in std_logic;
		RST	: in std_logic;
		Addr 	: out std_logic_vector(2 downto 0);
		putget: in std_logic;
		incget: in std_logic;
		incput: in std_logic;
		full 	: out std_logic;
		empty : out std_logic
   );
end component; 

component RAM is
	generic(
		ADDRESS_WIDTH : natural := 3;
		DATA_WIDTH : natural := 4
	);
	port(
		address : in std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
		wr: in std_logic;
		din: in std_logic_vector(DATA_WIDTH - 1 downto 0);
		dout: out std_logic_vector(DATA_WIDTH - 1 downto 0)
	);
end component;

begin
	FIFO_RAM : RAM port map(
		address 	=> saddr,
		wr 		=> sWr,
		din 		=> D,
		dout 		=> Q
	);
	
	MemoryAddressControl : MAC port map(
		clk 		=> clk,
		RST 		=> RST,
		Addr 		=> saddr,
		putget 	=> putget,
		incget 	=> incget,
		incput 	=> incput,
		full 		=> full,
		empty		=> empty
	);
	
	Control : RingBufferControl port map(
		clk 		=> clk,
		RST 		=> RST,
		DAV 		=> DAV,
		CTS 		=> CTS,
		full 		=> full,
		empty 	=> empty,
		Wr 		=> sWR,
		selPG 	=> putget,
		Wreg		=> Wreg,
		DAC 		=> DAC,
		incput 	=> incput,
		incget 	=> incget
	);
	

end architecture;