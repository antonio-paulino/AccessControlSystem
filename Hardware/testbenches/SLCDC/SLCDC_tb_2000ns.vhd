LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY SLCDC_tb is
end SLCDC_tb;

architecture teste of SLCDC_tb is
	component SLCDC is
		port (
			LCDsel: in STD_LOGIC;
			SCLK 	: in STD_LOGIC;
			SDX 	: in std_logic;
			clk	: IN std_logic;
			D 		: out std_logic_vector(4 downto 0);
			E 		: out STD_LOGIC;
			RST 	: in std_logic
		);	
	end component;
	
	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;
	
	signal clk_tb		: std_logic; 
	signal RST_tb		: std_logic;
	signal sdx_tb		: std_logic; 
	signal sclk_tb		: std_logic; 
	signal LCDsel_tb	: std_logic; 
	signal E_tb			: std_logic; 
	signal D_tb			: std_logic_vector(4 downto 0); 




	Begin 
	UUT: SLCDC 
			port map ( 
				LCDsel => LCDsel_tb,
				SCLK 	 => sCLK_tb, 
				SDX  	 => sdx_tb,
				clk 	 => clk_tb,
				D 		 => D_tb,
				E 		 => E_tb,
				RST 	 => RST_tb
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
			sdx_tb 	<= '0';
			sclk_tb <= '0';
			LCDsel_tb <= '0';
		--	Begin -- Inicialização do sistema
		   wait for MCLK_PERIOD*2;
			RST_tb 	<= '0';
			LCDsel_tb  <= '0';
			wait for MCLK_PERIOD*4;
			LCDsel_tb  <= '1';
			wait for MCLK_PERIOD*4;
		
			--Teste de sclks não suficientes
			LCDsel_tb  <= '0';
			sdx_tb 	<= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			LCDsel_tb <= '1';


			--Teste de sclks a mais
			wait for MCLK_PERIOD*4;
			LCDsel_tb <= '0';
			wait for MCLK_PERIOD*4;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			sdx_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			sdx_tb 	<= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD*3;
			LCDsel_tb  <= '1';
			
			
			--Teste de envio com sucesso
			wait for MCLK_PERIOD*3;
			LCDsel_tb <= '0';
			wait for MCLK_PERIOD*2;
			sclk_tb   <= '1';
			wait for MCLK_PERIOD;
			sclk_tb   <= '0';
			wait for MCLK_PERIOD;
			sclk_tb   <= '1';
			wait for MCLK_PERIOD;
			sclk_tb   <= '0';
			sdx_tb    <= '0';
			wait for MCLK_PERIOD;
			sclk_tb   <= '1';
			wait for MCLK_PERIOD;
			sclk_tb   <= '0';
			wait for MCLK_PERIOD;
			sclk_tb   <= '1';
			wait for MCLK_PERIOD;
			sclk_tb   <= '0';
			sdx_tb    <= '1';
			wait for MCLK_PERIOD;
			sclk_tb   <= '1';
			wait for MCLK_PERIOD;
			sclk_tb   <= '0';
			wait for MCLK_PERIOD*4;
			LCDsel_tb <= '1';
			wait;
			
			end process;
end teste;	