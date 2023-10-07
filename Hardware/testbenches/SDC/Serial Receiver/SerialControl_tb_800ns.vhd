LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY SerialControl_tb is
end SerialControl_tb;

architecture teste of SerialControl_tb is
	component SerialControl is
		port (
	      RST 		: in std_logic; 
			clk 		: in std_logic;
			enRX 		: in std_logic; 
			accept 	: in std_logic;
			eq5 		: in std_logic;
			clr 		: out std_logic;
			wr 		: out std_logic;
			DXval 	: out std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal clk_tb		: std_logic; 
	signal RST_tb		: std_logic;
	signal enRX_tb		: std_logic; 
	signal accept_tb	: std_logic; 
	signal eq5_tb		: std_logic; 
	signal clr_tb		: std_logic; 
	signal wr_tb		: std_logic; 
	signal DXval_tb	: std_logic; 




	Begin 
	UUT: SerialControl 
			port map ( 
				clk 	=> clk_tb,
				RST 	=> RST_tb,
				enRx 	=> enRX_tb,
				accept 	=> accept_tb,
				eq5 	=> eq5_tb,
				clr 	=> clr_tb,
				wr 		=> wr_tb,
				DXval 	=> Dxval_tb
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
			RST_tb 		<= '1';
			enRx_tb 	<= '0';
			eq5_tb 		<= '0';
			accept_tb 	<= '0';

		-- Inicialização
		   wait for MCLK_PERIOD*2;
			RST_tb 	<= '0';
			wait for MCLK_PERIOD*2;
			enRx_tb 	<= '0';
			wait for MCLK_PERIOD*2;
			enRx_tb 	<= '1';
			
		-- Teste de envio
			wait for MCLK_PERIOD*5;
			enRx_tb 	<= '0';
			wait for MCLK_PERIOD*10;
			eq5_tb 	<= '1';
			wait for MCLK_PERIOD*5;
			enRx_tb 	<= '1';
			wait for MCLK_PERIOD*5;
			accept_tb <= '1';
			wait;

		end process;
		
end teste;