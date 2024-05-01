LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY SDC_tb is
end SDC_tb;

architecture teste of SDC_tb is
	
	component SDC is
	PORT( SDCsel	: in STD_LOGIC;
			SCLK 		: in STD_LOGIC;
			SDX		: in std_logic;
			clk		: in std_logic;
			FCclose	: in std_logic;
			FCopen	: in std_logic;
			Pdetect	: in std_logic;
			OnOff 	: out STD_LOGIC;
			Dout 		: out std_logic_vector (4 downto 0);
			busy 		: out std_logic;
			RST		: in std_logic
	);	
	end component;

	
	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;
	
	signal clk_tb		: std_logic; 
	signal RST_tb		: std_logic;
	signal sdx_tb		: std_logic; 
	signal sclk_tb		: std_logic; 
	signal SDCsel_tb	: std_logic; 
	signal FCClose_tb	: std_logic; 
	signal FCOpen_tb	: std_logic; 
	signal Dout_tb 	: std_logic_vector(4 downto 0); 	
	signal busy_tb 	: std_logic;
	signal Pdetect_tb : std_logic;
	signal OnOff_tb	: std_logic;
	

	Begin 
	UUT: SDC 
			port map ( 
				SDCsel	=>	SDCsel_tb,
				SCLK		=> 	SCLK_tb,
				SDX		=>	SDX_tb,
				clk		=>	clk_tb,
				FCclose	=>	FCclose_tb,
				FCopen	=>	Fcopen_tb,
				Pdetect	=>	Pdetect_tb,
				OnOff		=> 	OnOff_tb,
				Dout		=> 	Dout_tb,
				busy 		=>	busy_tb,
				RST		=>	Rst_tb
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
			SDCsel_tb <= '0';
			FCClose_tb <= '0';
			FCopen_tb <= '0';
			Pdetect_tb <= '0';
			
		 --Fechar porta inicialmente
			WAIT FOR MCLK_PERIOD;
			FCclose_tb <= '1';
			WAIT FOR MCLK_PERIOD*3;
			
			
		--	Begin -- Inicialização do sistema
		   wait for MCLK_PERIOD*2;
			RST_tb 	<= '0';
			SDCsel_tb  <= '0';
			wait for MCLK_PERIOD*4;
			SDCsel_tb  <= '1';
			wait for MCLK_PERIOD*4;
		
			--Teste de sclks não suficientes
			SDCsel_tb  <= '0';
			sdx_tb 	<= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			sclk_tb <= '0';
			wait for MCLK_PERIOD;
			sclk_tb <= '1';
			wait for MCLK_PERIOD;
			SDCsel_tb <= '1';


			--Teste de sclks a mais
			wait for MCLK_PERIOD*4;
			SDCsel_tb <= '0';
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
			SDCsel_tb  <= '1';
			
			
		--Teste de comando de abertura 
			FCclose_tb <= '0';
			--Envio
			sdx_tb <= '1'; -- 1o bit a 1 para abrir
			wait for MCLK_PERIOD*3;
			SDCsel_tb <= '0';
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
			SDCsel_tb <= '1';
			
			-- Abertura
			wait for MCLK_PERIOD*10;
			FCclose_tb <= '1';
			wait for MCLK_PERIOD*10;
			FCopen_tb <= '1';
			wait for MCLK_PERIOD*3;
			
	--Teste de comando de fecho sem interrupções 
			
			--Envio
			FCopen_tb <= '0'; 
			FCclose_tb <= '0';
			sdx_tb <= '0'; -- 1o bit a 0 para fechar
			wait for MCLK_PERIOD*3;
			SDCsel_tb <= '0';
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
			SDCsel_tb <= '1';
			
			-- Fecho
			wait for MCLK_PERIOD*10;
			FCopen_tb <= '1';
			wait for MCLK_PERIOD*10;
			FCclose_tb <= '1';
			wait for MCLK_PERIOD*3;
			
	--Teste de comando de fecho com interrupção. Deixa de haver interrupção antes de a porta abrir outra vez.
			
			FCopen_tb <= '0'; 
			FCClose_tb <= '0';
			--Envio
			sdx_tb <= '0';
			wait for MCLK_PERIOD*3;
			SDCsel_tb <= '0';
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
			SDCsel_tb <= '1';
			
			-- Fecho
			wait for MCLK_PERIOD*5;
			FCopen_tb <= '1';
			wait for MCLK_PERIOD*4;
			FCopen_tb <= '0';
			wait for MCLK_PERIOD;
			Pdetect_tb <= '1';
			wait for MCLK_PERIOD*5;
			Pdetect_tb <= '0';
			wait for MCLK_PERIOD*5;
			FCopen_tb <= '1';
			wait for MCLK_PERIOD*10;
			FCopen_tb <= '0';
			wait for MCLK_PERIOD*5;
			FCopen_tb <= '1';
			wait for MCLK_PERIOD*5;
			FCclose_tb <= '1';
			wait for MCLK_PERIOD*3;
			
			
			
	--Teste de comando de fecho com interrupção. Continua a haver interrupção depois de a porta abrir outra vez.
	
			FCopen_tb <= '0'; 
			FCClose_tb <= '0';
			--Envio 
			sdx_tb <= '0';
			wait for MCLK_PERIOD*3;
			SDCsel_tb <= '0';
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
			SDCsel_tb <= '1';
			
			-- Fecho
			wait for MCLK_PERIOD*5;
			FCopen_tb <= '1';
			wait for MCLK_PERIOD*4;
			FCopen_tb <= '0';
			wait for MCLK_PERIOD;
			Pdetect_tb <= '1';
			wait for MCLK_PERIOD*10;
			FCopen_tb <= '1';
			wait for MCLK_PERIOD*10;
			Pdetect_tb <= '0';
			wait for MCLK_PERIOD*5;
			FCopen_tb <= '0';
			wait for MCLK_PERIOD*10;
			FCClose_tb <= '1';
			wait for MCLK_PERIOD*2;
		
			wait;
			
			end process;
end teste;	