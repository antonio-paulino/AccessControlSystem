LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY OutputBuffer_tb is
end OutputBufferl_tb;

architecture teste of OutputBuffer_tb is
	component OutputBuffer is
	port (
		D	 : in std_logic_vector(3 downto 0);
		clk : in std_logic;
		RST : in std_logic;
		Load: in std_logic;
		ACK : in std_logic;
		Obfree : out std_logic;
		Dval	: out std_logic;
		Q 		: out std_logic_vector(3 downto 0)
	);
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal D_tb 		: std_logic;
	signal RST_tb		: std_logic; 
	signal clk_tb		: std_logic;
	signal Load_tb		: std_logic;
	signal ACK_tb		: std_logic; 
	signal Obfree_tb	: std_logic; 
	signal Dval_tb		: std_logic;


Begin 
	UUT: OutBufferControl 
		port map ( 
			clk 	=> clk_tb,
			RST 	=> rst_tb,
			Load 	=> load_tb,
			ACK 	=> ack_tb,
			Obfree 	=> obfree_tb,
			Dval 	=> dval_tb,
			D 	=> D_tb
		);
	clk_gen : process
		Begin
			clk_tb <= '0';
			wait for MCLK_HALF_PERIOD;
			clk_tb <= '1';
			wait for MCLK_HALF_PERIOD;
		end process;
	stimulus: process
	begin
	--reset
		rst_tb <= '1';
    	Load_tb <= '0';
    	ack_tb <= '0';
	--Begin
    	WAIT FOR MCLK_PERIOD;
    	rst_tb <= '0';
		D <= "1010";
		WAIT FOR MCLK_PERIOD*3;
    	load_tb <= '1';
    	WAIT FOR MCLK_PERIOD*3;
    	load_tb <= '0';
    	WAIT FOR MCLK_PERIOD*3;
    	ack_tb <= '1';
    	WAIT FOR MCLK_PERIOD*3;
    	ack_tb <= '0';
		WAIT FOR MCLK_PERIOD*3
		D <= "0110";
		WAIT FOR MCLK_PERIOD*3;
    	load_tb <= '1';
    	WAIT FOR MCLK_PERIOD*3;
    	load_tb <= '0';
    	WAIT FOR MCLK_PERIOD*3;
    	ack_tb <= '1';
    	WAIT FOR MCLK_PERIOD*3;
    	ack_tb <= '0';
  
		wait;
		end process;
	end teste;