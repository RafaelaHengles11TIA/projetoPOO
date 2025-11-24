import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;

public class Main {

    public interface ImpressoraDLL extends Library {

        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "C:\\Users\\rafaela_hengles\\Downloads\\Java-Aluno EM\\Java-Aluno EM\\Java-Aluno EM\\E1_Impressora01.dll",
                ImpressoraDLL.class
        );

        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);

        int FechaConexaoImpressora();

        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);

        int Corte(int avanco);

        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao);

        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI);

        int AvancaPapel(int linhas);

        int StatusImpressora(int param);

        int AbreGavetaElgin();

        int AbreGaveta(int pino, int ti, int tf);

        int SinalSonoro(int qtd, int tempoInicio, int tempoFim);

        int ModoPagina();

        int LimpaBufferModoPagina();

        int ImprimeModoPagina();

        int ModoPadrao();

        int PosicaoImpressaoHorizontal(int posicao);

        int PosicaoImpressaoVertical(int posicao);

        int ImprimeXMLSAT(String dados, int param);

        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param);
    }

    private static boolean conexaoAberta = false;
    private static int tipo;
    private static String modelo;
    private static String conexao;
    private static int parametro;
    private static final Scanner scanner = new Scanner(System.in);

    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }


    public static void configurarConexao() {
        if (!conexaoAberta) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n=== CONFIGURAÇÃO DA CONEXÃO ===");

            System.out.println("Digite o tipo de conexão (ex: 1 para USB, 2 para Serial): ");
            tipo = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite o modelo da impressora (ex: MP-4200): ");
            modelo = scanner.nextLine();

            System.out.println("Digite o identificador da conexão (ex: USB, COM3, IP da impressora): ");
            conexao = scanner.nextLine();


            parametro = 0;

            System.out.println("Configuração realizada com sucesso!");
        } else {
            System.out.println("A conexão já está aberta. Feche antes de reconfigurar.");
        }
    }

    public static void abrirConexao() {
        if (!conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);
            if (retorno == 0) {
                conexaoAberta = true;
                System.out.println("Conexão aberta com sucesso.");
            } else {
                System.out.println("Erro ao abrir conexão. Código de erro: " + retorno);
            }
        } else {
            System.out.println("Conexão já está aberta.");
        }
    }



    public static void fecharConexao () {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();
            if (retorno == 0) {
                conexaoAberta = false;
                System.out.println("Conexão fechada com sucesso.");
            } else {
                System.out.println("Erro ao fechar conexão. Código: " + retorno);
            }
        } else {
            System.out.println("Conexão já está fechada.");
        }
    }

    public static void ImpressaoTexto() {
        if (conexaoAberta) {
            String texto = capturarEntrada("Digite o texto para impressão: ");
            ImpressoraDLL.INSTANCE.ImpressaoTexto(texto, 1, 0, 0);
            ImpressoraDLL.INSTANCE.AvancaPapel(3);
            ImpressoraDLL.INSTANCE.Corte(2);
            System.out.println("Texto impresso.");
        } else System.out.println("Erro: Conexão não está aberta.");
    }

    public static void Corte() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.Corte(5);

            if (retorno == 0) {
                System.out.println("Corte realizado com sucesso!");
            } else {
                System.out.println("Erro ao cortar papel. Código: " + retorno);
            }
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }


    public static void ImpressaoQRcode() {
        if (conexaoAberta) {
            String dados = capturarEntrada("Digite o texto para gerar o QR Code: ");

            int tamanho = 6;
            int nivelCorrecao = 4;

            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, tamanho, nivelCorrecao);

            if (retorno == 0) {
                System.out.println("QR Code impresso com sucesso!");
                ImpressoraDLL.INSTANCE.AvancaPapel(2);
                ImpressoraDLL.INSTANCE.Corte(2);
            } else {
                System.out.println("Erro ao imprimir QR Code. Código de erro: " + retorno);
            }
        } else {
            System.out.println("Erro: conexão com a impressora não está aberta.");
        }
    }

    public static void ImpressaoCodigoBarras() {
        if (conexaoAberta) {
            ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(8, "{A12345678912}", 100, 2, 3);
            ImpressoraDLL.INSTANCE.AvancaPapel(3);
            //ImpressoraDLL.INSTANCE.Corte(2);
            System.out.println("Código de barras impresso.");
        } else System.out.println("Erro: Conexão não está aberta.");
    }

    public static void AvancaPapel() {
        if (conexaoAberta) {
            System.out.print("Quantas linhas deseja avançar? ");
            int linhas = scanner.nextInt();
            scanner.nextLine();

            int retorno = ImpressoraDLL.INSTANCE.AvancaPapel(linhas);

            if (retorno == 0) {
                System.out.println("Papel avançado com sucesso!");
            } else {
                System.out.println("Erro ao avançar papel. Código: " + retorno);
            }
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    public static void AbreGaveta() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(1, 5, 10);

            if (retorno == 0) {
                System.out.println("Gaveta aberta com sucesso!");
            } else {
                System.out.println("Erro ao abrir gaveta. Código de erro: " + retorno);
            }
        } else {
            System.out.println("A Conexão está fechada.");
        }
    }


    public static void AbreGavetaElgin() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreGavetaElgin();
            if (retorno == 0) {
                System.out.println("Gaveta Elgin aberta com sucesso!");
            } else {
                System.out.println("Erro ao abrir gaveta Elgin. Código de erro: " + retorno);
            }
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    public static void SinalSonoro() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(4,5,5);

            if (retorno == 0) {
                System.out.println("Sinal emitido com sucesso!");
            } else {
                System.out.println("Erro ao emitir sinal. Código de erro: " + retorno);
            }
        } else {
            System.out.println("A Conexão está fechada.");
        }
    }

    public static void ImprimeXMLSAT() {
        if (conexaoAberta) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos XML", "xml"));

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();

                try {
                    String conteudoXML = lerArquivoComoString(path);

                    int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(conteudoXML, 0);

                    if (retorno == 0) {
                        System.out.println("Impressão do XML de venda realizada com sucesso!");
                        ImpressoraDLL.INSTANCE.AvancaPapel(3);
                        ImpressoraDLL.INSTANCE.Corte(5);
                    } else {
                        System.out.println("Erro ao imprimir XML SAT. Código de erro: " + retorno);
                    }

                } catch (IOException e) {
                    System.out.println("Erro ao ler o arquivo XML: " + e.getMessage());
                }
            } else {
                System.out.println("Nenhum arquivo selecionado.");
            }
        } else {
            System.out.println("Erro: conexão com a impressora não está aberta.");
        }
    }

    public static void ImprimeXMLCancelamentoSAT() {
        if (conexaoAberta) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos XML", "xml"));

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();

                try {
                    String conteudoXML = lerArquivoComoString(path);

                    String assQRCode = "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

                    int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(conteudoXML, assQRCode, 0);

                    if (retorno == 0) {
                        System.out.println("Impressão do XML de cancelamento realizada com sucesso!");
                        ImpressoraDLL.INSTANCE.AvancaPapel(3);
                        ImpressoraDLL.INSTANCE.Corte(5);
                    } else {
                        System.out.println("Erro ao imprimir XML de cancelamento SAT. Código de erro: " + retorno);
                    }

                } catch (IOException e) {
                    System.out.println("Erro ao ler o arquivo XML: " + e.getMessage());
                }
            } else {
                System.out.println("Nenhum arquivo selecionado.");
            }
        } else {
            System.out.println("Erro: conexão com a impressora não está aberta.");
        }
    }
    public static void main (String[]args){
        while (true) {
            System.out.println("\n*************************************************");
            System.out.println("**************** MENU IMPRESSORA *******************");
            System.out.println("*************************************************\n");

            System.out.println("1  - Configurar Conexao");
            System.out.println("2  - Abrir Conexao");
            System.out.println("3  - Impressao Texto");
            System.out.println("4  - Impressao QRCode");
            System.out.println("5  - Impressao Cod Barras");
            System.out.println("6  - Impressao XML SAT");
            System.out.println("7  - Impressao XML Canc SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta");
            System.out.println("10  - Sinal Sonoro");
            System.out.println("0  - Fechar Conexao e Sair");


            String escolha = capturarEntrada("\nDigite a opção desejada: ");

            if (escolha.equals("0")) {
                fecharConexao();
                System.out.println("Programa encerrado.");
                break;
            }

            switch (escolha) {
                case "1":
                    configurarConexao();
                    break;
                case "2":
                    abrirConexao();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;
                case "3":
                    ImpressaoTexto();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;

                case "4":
                    ImpressaoQRcode();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;
                case "5":
                    ImpressaoCodigoBarras();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;

                case "6":
                    ImprimeXMLSAT();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;

                case "7":
                    ImprimeXMLCancelamentoSAT();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;


                case "8":
                    AbreGavetaElgin();
                    break;

                case "9":
                    AbreGaveta();
                    break;

                case "10":
                    SinalSonoro();
                    break;

                default:
                    System.out.println("OPÇÃO INVÁLIDA");
            }
        }

        scanner.close();
    }

    private static String lerArquivoComoString (String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }
}