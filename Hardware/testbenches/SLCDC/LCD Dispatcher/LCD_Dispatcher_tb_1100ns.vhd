LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY LCD_Dispatcher_tb is
end LCD_Dispatcher_tb;

architecture teste of LCD_Dispatcher_tb is
	component LCD_Dispatcher is
		port (
			clk 	: in std_logic;
			RST 	: in std_logic;
			Dval	: in std_logic; 
			Din 	: in std_logic_vector ( 4 downto 0);
			WrL 	: out std_logic;
			Dout	: out std_logic_vector (4 downto 0);
			done	: out std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal clk_tb	: std_logic; 
	signal RST_tb	: std_logic;
	signal Dval_tb	: std_logic; 
	signal Din_tb	: std_logic_vector (4 downto 0);
	signal Wrl_tb	: std_logic;
	signal Dout_tb	: std_logic_vector (4 downto 0);
	signal done_tb	: std_logic;



	Begin 
	UUT: LCD_Dispatcher 
			port map ( 
				clk 	=> clk_tb,
				RST 	=> RST_tb,
				Dval 	=> Dval_tb,
				Din 	=> Din_tb,
				Wrl 	=> Wrl_tb,
				Dout 	=> Dout_tb,
				done 	=> done_tb
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
		--	reset
			RST_tb 	<= '1';
			Din_tb 	<= "11111";
			Dval_tb <= '0';
		--	Begin
		   wait for MCLK_PERIOD*2;
			RST_tb 	<= '0';
			Din_tb 	<= "01101";
			wait for MCLK_PERIOD*2;
			Dval_tb <= '1';
			wait for MCLK_PERIOD*20;
			Dval_tb <= '0';
			wait for MCLK_PERIOD*5;
			Din_tb 	<= "11001";
			wait for MCLK_PERIOD*2;
			Dval_tb <= '1';
			wait for MCLK_PERIOD*20;
			Dval_tb <= '0';
			wait;

			end process;
end teste;