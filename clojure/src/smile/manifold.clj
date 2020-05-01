;   Copyright (c) 2010-2020 Haifeng Li. All rights reserved.
;
;   Smile is free software: you can redistribute it and/or modify
;   it under the terms of the GNU Lesser General Public License as
;   published by the Free Software Foundation, either version 3 of
;   the License, or (at your option) any later version.
;
;   Smile is distributed in the hope that it will be useful,
;   but WITHOUT ANY WARRANTY; without even the implied warranty of
;   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;   GNU Lesser General Public License for more details.
;
;   You should have received a copy of the GNU Lesser General Public License
;   along with Smile.  If not, see <https://www.gnu.org/licenses/>.

(ns smile.manifold
  "Manifold Learning"
  {:author "Haifeng Li"}
  (:import [smile.manifold IsoMap LLE LaplacianEigenmap TSNE UMAP]))

(defn isomap
  "Isometric feature mapping.

  Isomap is a widely used low-dimensional embedding methods,
  where geodesic distances on a weighted graph are incorporated with the
  classical multidimensional scaling. Isomap is used for computing a
  quasi-isometric, low-dimensional embedding of a set of high-dimensional
  data points. Isomap is highly efficient and generally applicable to a broad
  range of data sources and dimensionalities.

  To be specific, the classical MDS performs low-dimensional embedding based
  on the pairwise distance between data points, which is generally measured
  using straight-line Euclidean distance. Isomap is distinguished by
  its use of the geodesic distance induced by a neighborhood graph
  embedded in the classical scaling. This is done to incorporate manifold
  structure in the resulting embedding. Isomap defines the geodesic distance
  to be the sum of edge weights along the shortest path between two nodes.
  The top n eigenvectors of the geodesic distance matrix, represent the
  coordinates in the new n-dimensional Euclidean space.

  The connectivity of each data point in the neighborhood graph is defined
  as its nearest k Euclidean neighbors in the high-dimensional space. This
  step is vulnerable to 'short-circuit errors' if k is too large with
  respect to the manifold structure or if noise in the data moves the
  points slightly off the manifold. Even a single short-circuit error
  can alter many entries in the geodesic distance matrix, which in turn
  can lead to a drastically different (and incorrect) low-dimensional
  embedding. Conversely, if k is too small, the neighborhood graph may
  become too sparse to approximate geodesic paths accurately.

  This class implements C-Isomap that involves magnifying the regions
  of high density and shrink the regions of low density of data points
  in the manifold. Edge weights that are maximized in Multi-Dimensional
  Scaling(MDS) are modified, with everything else remaining unaffected.

  `data` is the data set.
  `d` is the dimension of the manifold.
  `k` is the number of nearest neighbors.
  If `c-isomap` is true, run C-Isomap algorithm. Otherwise standard algorithm."
  ([data k] (isomap data k 2 true))
  ([data k d c-isomap] (IsoMap/of data k d c-isomap)))

(defn lle
  "Locally Linear Embedding.

  LLE has several advantages over Isomap, including faster optimization
  when implemented to take advantage of sparse matrix algorithms, and better
  results with many problems. LLE also begins by finding a set of the nearest
  neighbors of each point. It then computes a set of weights for each point
  that best describe the point as a linear combination of its neighbors.
  Finally, it uses an eigenvector-based optimization technique to find the
  low-dimensional embedding of points, such that each point is still described
  with the same linear combination of its neighbors. LLE tends to handle
  non-uniform sample densities poorly because there is no fixed unit to
  prevent the weights from drifting as various regions differ in sample
  densities.

  `data` is the data set.
  `d` is the dimension of the manifold.
  `k` is the number of nearest neighbors."
  ([data k] (lle data k 2 true))
  ([data k d] (LLE/of data k d)))

(defn laplacian
  "Laplacian Eigenmap.

  Using the notion of the Laplacian of the nearest neighbor adjacency graph,
  Laplacian Eigenmap compute a low dimensional representation of the dataset
  that optimally preserves local neighborhood information in a certain sense.
  The representation map generated by the algorithm may be viewed as a
  discrete approximation to a continuous map that naturally arises from
  the geometry of the manifold.

  The locality preserving character of the Laplacian Eigenmap algorithm makes
  it relatively insensitive to outliers and noise. It is also not prone to
  'short circuiting' as only the local distances are used.

  `data` is the data set.
  `d` is the dimension of the manifold.
  `k` is the number of nearest neighbors.
  `t` is the smooth/width parameter of heat kernel
  e<sup>-||x-y||<sup>2</sup> / t</sup>. Non-positive value means
  discrete weights."
  ([data k] (laplacian data k 2 -1.0))
  ([data k d t] (LaplacianEigenmap/of data k d t)))

(defn tsne
  "t-distributed stochastic neighbor embedding.

  t-SNE is a nonlinear dimensionality reduction technique that is
  particularly well suited for embedding high-dimensional data into
  a space of two or three dimensions, which can then be visualized
  in a scatter plot. Specifically, it models each high-dimensional
  object by a two- or three-dimensional point in such a way that
  similar objects are modeled by nearby points and dissimilar objects
  are modeled by distant points.

  `X` is input data. If X is a square matrix, it is assumed to be the
  squared distance/dissimilarity matrix.
  `d` is the dimension of the manifold.
  `perplexity` is the perplexity of the conditional distribution.
  `eta` is the learning rate.
  `iterations` is the number of iterations."
  ([data] (tsne data 2 20.0 200.0 1000))
  ([data d perplexity eta iterations] (TSNE. data d perplexity eta iterations)))

(defn umap
  "Unnifold Approximation and Projection.

  UMAP is a dimension reduction technique that can be used for visualization
  similarly to t-SNE, but also for general non-linear dimension reduction.
  The algorithm is founded on three assumptions about the data:
  
   - The data is uniformly distributed on a Riemannian manifold;
   - The Riemannian metric is locally constant (or can be approximated as such);
   - The manifold is locally connected.
  
  From these assumptions it is possible to model the manifold with a fuzzy
  topological structure. The embedding is found by searching for a low
  dimensional projection of the data that has the closest possible equivalent
  fuzzy topological structure.

  `data` is the input data.
  `distance` is the distance measure.
  `k` is of k-nearest neighbors. Larger values result in more global views
  of the manifold, while smaller values result in more local data
  being preserved. Generally in the range 2 to 100.
  `d` is the target embedding dimensions. defaults to 2 to provide easy
  visualization, but can reasonably be set to any integer value
  in the range 2 to 100.
  `iterations` is the number of iterations to optimize the
  low-dimensional representation. Larger values result in more
  accurate embedding. Muse be at least 10. Choose wise value
  based on the size of the input data, e.g, 200 for large
  data (1000+ samples), 500 for small.
  `learningRate` is the initial learning rate for the embedding optimization,
  default 1.
  `minDist` is the desired separation between close points in the embedding
  space. Smaller values will result in a more clustered/clumped
  embedding where nearby points on the manifold are drawn closer
  together, while larger values will result on a more even
  disperse of points. The value should be set no-greater than
  and relative to the spread value, which determines the scale
  at which embedded points will be spread out. default 0.1.
  `spread` is the effective scale of embedded points. In combination with
  minDist, this determines how clustered/clumped the embedded
  points are. default 1.0.
  `negativeSamples` is the number of negative samples to select per positive
  sample in the optimization process. Increasing this value will result
  in greater repulsive force being applied, greater optimization
  cost, but slightly more accuracy, default 5.
  `repulsionStrength` is the weight applied to negative samples in low
  dimensional embedding optimization. Values higher than one will result in
  greater weight being given to negative samples, default 1.0."
  ([data] (UMAP/of data))
  ([data distance] (UMAP/of data distance))
  ([data k d iterations learningRate minDist spread negativeSamples repulsionStrength] (UMAP/of data k d iterations learningRate minDist spread negativeSamples repulsionStrength))
  ([data distance k d iterations learningRate minDist spread negativeSamples repulsionStrength] (UMAP/of data distance k d iterations learningRate minDist spread negativeSamples repulsionStrength)))