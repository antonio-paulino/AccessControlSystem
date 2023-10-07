LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY SerialReceiver_tb is
end SerialReceiver_tb;

architecture teste of SerialReceiver_tb is
	component SerialReceiver is
		port (
			busy	: out std_logic;
	      	SDX 	: in STD_LOGIC;
			SCLK 	: in STD_LOGIC;
			SS 		: in std_logic;
			accept 	: IN STD_LOGIC;
			clk		: IN std_logic;
			D 		: out std_logic_vector(4 downto 0);
			DXval 	: out STD_LOGIC;
			RST		: in std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal clk_tb		: std_logic; 
	signal RST_tb		: std_logic;
	signal sdx_tb		: std_logic; 
	signal sclk_tb		: std_logic; 
	signal ss_tb		: std_logic; 
	signal accept_tb	: std_logic; 
	signal D_tb			: std_logic_vector(4 downto 0); 
	signal DXval_tb		: std_logic; 
	signal busy_tb		: std_logic;




	Begin 
	UUT: SerialReceiver 
			port map ( 
				busy 	=> busy_tb,
				SS 		=> ss_tb,
				SCLK 	=> SCLK_tb,
				SDX 	=> SDX_tb,
				accept 	=> accept_tb,
				clk 	=> clk_tb,
				D 		=> D_tb,
				DXval 	=> DXval_tb,
				RST		=> RST_tb
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
			ss_tb 	<= '0';
			accept_tb <= '0';
		--	Begin -- Inicialização do sistema
		   wait for MCLK_PERIOD*2;
			RST_tb <= '0';
			SS_tb  <= '0';
			wait for MCLK_PERIOD*4;
			SS_tb  <= '1';
			wait for MCLK_PERIOD*4;
		
			--Teste de sclks não suficientes
			SS_tb  	<= '0';
			sdx_tb 	<= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			SS_tb 	<= '1';


			--Teste de sclks a mais
			wait for MCLK_PERIOD*4;
			SS_tb 	<= '0';
			wait for MCLK_PERIOD*4;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			sdx_tb 	<= '0';
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
			SS_tb  	<= '1';
			
			
			--Teste de envio com sucesso
			wait for MCLK_PERIOD*3;
			SS_tb 	<= '0';
			wait for MCLK_PERIOD*2;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			sdx_tb 	<= '0';
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
			wait for MCLK_PERIOD*4;
			SS_tb 	<= '1';
			wait for MCLK_PERIOD*2;
			accept_tb <= '1';
			wait;
			
		end process;
end teste;