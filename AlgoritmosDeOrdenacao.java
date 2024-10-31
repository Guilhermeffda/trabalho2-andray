import java.util.Random;
import java.util.Arrays;

public class AlgoritmosDeOrdenacao {
    private static final int[] TAMANHOS = {1000, 10000, 100000, 500000, 1000000};
    private static final int RODADAS = 5;

    public static void main(String[] args) {
        for (int tamanho : TAMANHOS) {
            System.out.println("Tamanho do vetor: " + tamanho);
            long tempoTotalMerge = 0, tempoTotalRadix = 0;
            long trocasTotaisMerge = 0, trocasTotaisRadix = 0;
            long iteracoesTotaisMerge = 0, iteracoesTotaisRadix = 0;

            for (int rodada = 0; rodada < RODADAS; rodada++) {
                int[] vetor = gerarVetorAleatorio(tamanho, rodada);
                int[] vetorCopia = Arrays.copyOf(vetor, vetor.length);

                Resultado resultadoMerge = ordenacaoMerge(vetor);
                Resultado resultadoRadix = ordenacaoRadix(vetorCopia);

                tempoTotalMerge += resultadoMerge.tempo;
                trocasTotaisMerge += resultadoMerge.trocas;
                iteracoesTotaisMerge += resultadoMerge.iteracoes;

                tempoTotalRadix += resultadoRadix.tempo;
                trocasTotaisRadix += resultadoRadix.trocas;
                iteracoesTotaisRadix += resultadoRadix.iteracoes;
            }

            System.out.println("Merge Sort - Tempo Médio: " + (tempoTotalMerge / RODADAS) + "ms, Trocas Médias: " + (trocasTotaisMerge / RODADAS) + ", Iterações Médias: " + (iteracoesTotaisMerge / RODADAS));
            System.out.println("Radix Sort - Tempo Médio: " + (tempoTotalRadix / RODADAS) + "ms, Trocas Médias: " + (trocasTotaisRadix / RODADAS) + ", Iterações Médias: " + (iteracoesTotaisRadix / RODADAS));
            System.out.println();
        }
    }

    private static int[] gerarVetorAleatorio(int tamanho, int semente) {
        Random aleatorio = new Random(semente);
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1000000);
        }
        return vetor;
    }

    private static Resultado ordenacaoMerge(int[] vetor) {
        long tempoInicio = System.currentTimeMillis();
        long trocas = 0;
        long iteracoes = 0;

        Resultado resultado = auxiliarOrdenacaoMerge(vetor, 0, vetor.length - 1);
        trocas += resultado.trocas;
        iteracoes += resultado.iteracoes;

        long tempoFim = System.currentTimeMillis();
        return new Resultado(tempoFim - tempoInicio, trocas, iteracoes);
    }

    private static Resultado auxiliarOrdenacaoMerge(int[] vetor, int esquerda, int direita) {
        long trocas = 0;
        long iteracoes = 0;

        if (esquerda < direita) {
            int meio = (esquerda + direita) / 2;

            Resultado resultadoEsquerda = auxiliarOrdenacaoMerge(vetor, esquerda, meio);
            Resultado resultadoDireita = auxiliarOrdenacaoMerge(vetor, meio + 1, direita);

            Resultado resultadoMescla = mesclar(vetor, esquerda, meio, direita);

            trocas += resultadoEsquerda.trocas + resultadoDireita.trocas + resultadoMescla.trocas;
            iteracoes += resultadoEsquerda.iteracoes + resultadoDireita.iteracoes + resultadoMescla.iteracoes;
        }

        return new Resultado(0, trocas, iteracoes);
    }

    private static Resultado mesclar(int[] vetor, int esquerda, int meio, int direita) {
        int n1 = meio - esquerda + 1;
        int n2 = direita - meio;

        int[] E = new int[n1];
        int[] D = new int[n2];

        for (int i = 0; i < n1; ++i)
            E[i] = vetor[esquerda + i];
        for (int j = 0; j < n2; ++j)
            D[j] = vetor[meio + 1 + j];

        int i = 0, j = 0, k = esquerda;
        long trocas = 0, iteracoes = 0;

        while (i < n1 && j < n2) {
            iteracoes++;
            if (E[i] <= D[j]) {
                vetor[k] = E[i];
                i++;
            } else {
                vetor[k] = D[j];
                j++;
                trocas++;
            }
            k++;
        }

        while (i < n1) {
            iteracoes++;
            vetor[k] = E[i];
            i++;
            k++;
        }

        while (j < n2) {
            iteracoes++;
            vetor[k] = D[j];
            j++;
            k++;
        }

        return new Resultado(0, trocas, iteracoes);
    }

    private static Resultado ordenacaoRadix(int[] vetor) {
        long tempoInicio = System.currentTimeMillis();
        long trocas = 0;
        long iteracoes = 0;

        int maximo = Arrays.stream(vetor).max().getAsInt();

        for (int exp = 1; maximo / exp > 0; exp *= 10) {
            Resultado resultadoContagem = ordenacaoContagem(vetor, exp);
            trocas += resultadoContagem.trocas;
            iteracoes += resultadoContagem.iteracoes;
        }

        long tempoFim = System.currentTimeMillis();
        return new Resultado(tempoFim - tempoInicio, trocas, iteracoes);
    }

    private static Resultado ordenacaoContagem(int[] vetor, int exp) {
        int n = vetor.length;
        int[] saida = new int[n];
        int[] contagem = new int[10];
        Arrays.fill(contagem, 0);

        long trocas = 0;
        long iteracoes = 0;

        for (int i = 0; i < n; i++) {
            iteracoes++;
            contagem[(vetor[i] / exp) % 10]++;
        }

        for (int i = 1; i < 10; i++) {
            iteracoes++;
            contagem[i] += contagem[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            iteracoes++;
            saida[contagem[(vetor[i] / exp) % 10] - 1] = vetor[i];
            contagem[(vetor[i] / exp) % 10]--;
            trocas++;
        }

        for (int i = 0; i < n; i++) {
            iteracoes++;
            vetor[i] = saida[i];
        }

        return new Resultado(0, trocas, iteracoes);
    }

    private static class Resultado {
        long tempo;
        long trocas;
        long iteracoes;

        Resultado(long tempo, long trocas, long iteracoes) {
            this.tempo = tempo;
            this.trocas = trocas;
            this.iteracoes = iteracoes;
        }
    }
}