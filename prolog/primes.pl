has_divisor(N, D) :- D * D =< N, (0 is mod(N, D); D1 is D + 1, has_divisor(N, D1)).

prime(2).
prime(N) :- N > 2, \+ has_divisor(N, 2).

composite(N) :- \+ prime(N).

validate_div_list([]).
validate_div_list([X]) :- prime(X).
validate_div_list([H, N | T]) :- H =< N, prime(H), validate_div_list([N | T]).

prime_divisors(1, []) :- !.
prime_divisors(N, Divisors) :- number(N), N > 1, !, find_divisors(N, 2, Divisors), validate_div_list(Divisors).
prime_divisors(N, Divisors) :- var(N), !, divs_prod(Divisors, N), validate_div_list(Divisors).

find_divisors(1, _, []) :- !.
find_divisors(N, H, [H | T]) :- 0 is mod(N, H), ND is div(N, H), !, find_divisors(ND, H, T).
find_divisors(N, F, Divisors) :- next_prime(F, FP), find_divisors(N, FP, Divisors).

next_prime(F, R) :- R is F + 1, prime(R), !.
next_prime(F, R) :- F1 is F + 1, next_prime(F1, R).

divs_prod([], 1).
divs_prod([H | T], N) :- divs_prod(T, NT), N is H * NT.

prime_index(P, I) :- prime(P), prime_idx_rec(2, P, 1, I).
prime_idx_rec(C, C, I, I).
prime_idx_rec(C, T, CI, I) :- C < T, next_prime(C, N), NI is CI + 1, prime_idx_rec(N, T, NI, I).
