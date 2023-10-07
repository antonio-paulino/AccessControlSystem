LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY KeyBoardReader_tb is
end KeyBoardReader_tb;

architecture teste of KeyBoardReader_tb is
	component KeyBoardReader is
	PORT( 
			RST 	: IN STD_LOGIC;
			Osc		: in std_logic;
			ACK		: IN std_logic;
			Lines 	: in std_logic_vector(3 downto 0);
			Q 		: out std_logic_vector(3 downto 0);
			Columns	: out std_logic_vector(2 downto 0);
			Dval	: out std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;


	signal Kack_tb		: std_logic;
	signal osc_tb		: std_logic; 
	signal RST_tb		: std_logic; 
	signal lines_tb 	: std_logic_vector(3 downto 0);
	signal Q_tb 		: std_logic_vector(3 downto 0);
	signal Columns_tb 	: std_logic_vector(2 downto 0);
	signal Dval_tb 		: std_logic;

Begin 
	UUT: KeyBoardReader 
		port map ( 
			RST  	=> RST_tb,
			Osc  	=> osc_tb,
			ACK 	=> Kack_tb,
			Lines  	=> lines_tb,
			Q  		=> Q_tb, 
			Columns => COlumns_tb,
			Dval 	=> Dval_tb
		);
	clk_gen : process
		Begin
			osc_tb <= '0';
			wait for MCLK_HALF_PERIOD;
			osc_tb <= '1';
			wait for MCLK_HALF_PERIOD;
		end process;

	stimulus: process
	begin
		--reset
		RST_tb <= '1';
		Kack_tb <= '0';
		--Begin
		
		--Escrita na memória
		wait for MCLK_PERIOD*2;
		RST_tb   <= '0';
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";	
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		
		--Leitura
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0';
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1';
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0'; 
		
		--Escrita e leitura sucessivas
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0';
		wait for MCLK_PERIOD*5;
		lines_tb <= "1110";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0';
		wait for MCLK_PERIOD*5;
		lines_tb <= "1011";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0';
		wait for MCLK_PERIOD*5;
		lines_tb <= "1110";
		wait for MCLK_PERIOD*5;
		lines_tb <= "1111";
		wait for MCLK_PERIOD*5;
		Kack_tb <= '1'; 
		wait for MCLK_PERIOD*3;
		Kack_tb <= '0';
		
		wait;
		end process;
end teste;