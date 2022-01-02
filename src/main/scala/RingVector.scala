import Ordering.Implicits.*

trait RingVector:

  type Index = Int
  type IndexO = Int

  extension[A](ring: Vector[A])
  
    private def index(i: IndexO): Index =
      java.lang.Math.floorMod(i, ring.size)

    def applyO(i: IndexO): A =
      ring(index(i))

    def rotateRight(step: Int): Vector[A] = 
      if ring.isEmpty then ring
      else
        val j: Index = ring.size - index(step) 
        ring.drop(j) ++ ring.take(j)

    def startAt(i: IndexO): Vector[A] =
      rotateRight(-i)

    def reflectAt(i: IndexO = 0): Vector[A] =
      startAt(i + 1).reverse

    def multiply(times: Int): Vector[A] =
      List.fill(times)(ring).toVector.flatten

    def sliceO(from: IndexO, to: IndexO): Vector[A] =
      if from >= to || ring.isEmpty then ring
      else
        val length = to - from
        val times = Math.ceil(length / ring.size).toInt + 1
        startAt(from).multiply(times).take(length)

    def slidingO(size: Int): Iterator[Vector[A]] =
      sliceO(0, ring.size + size - 1).sliding(size)

    def allRotations: Iterator[Vector[A]] =
      slidingO(ring.size)

    def minRotation(implicit ordering: Ordering[Vector[A]]): Vector[A] =
      allRotations.min(ordering)

    def isRotationOf(other: Vector[A]): Boolean =
      allRotations.exists(_ == other)

    def isReflectionOf(other: Vector[A]): Boolean =
      ring.reflectAt() == other

    def isRotationOrReflectionOf(other: Vector[A]): Boolean =
      val reflected = other.reverse
      allRotations.exists(r => r == other || r == reflected)

    private def areFoldsSymmetrical: Int => Boolean =
      n => rotateRight(ring.size / n) == ring

    def rotationalSymmetry: Int =
      val size = ring.size
      val exactFoldsDesc = size +: (size / 2 to 2 by -1).filter(size % _ == 0)
      exactFoldsDesc.find(areFoldsSymmetrical).getOrElse(1)

    def rotationalSymmetryTiling: Int =
      val size = ring.size
      val tilingFoldsDesc = size :: List(12, 6, 4, 3, 2).filter(fold => fold < size && size % fold == 0)
      tilingFoldsDesc.find(areFoldsSymmetrical).getOrElse(1)

    private def greaterHalfSize: Int =
      Math.ceil(ring.size / 2.0).toInt

    private def checkReflectionAxis(gap: Int): Boolean =
      (0 until greaterHalfSize).forall(j => applyO(j + 1) == applyO(-(j + gap)))

    private def hasHeadOnAxis: Boolean =
      checkReflectionAxis(1)

    private def hasAxisBetweenHeadAndNext: Boolean =
      checkReflectionAxis(0)

    def findReflectionSymmetry: Option[Index] =
      (0 until greaterHalfSize).find(j =>
        val rotation = startAt(j)
        rotation.hasHeadOnAxis || rotation.hasAxisBetweenHeadAndNext
      )
   
    def symmetryIndices: List[Index] =
      val folds = rotationalSymmetry
      val foldSize = ring.size / folds
      ring.take(foldSize).findReflectionSymmetry match
        case None => Nil
        case Some(j) => (0 until folds).toList.map(_ * foldSize + j)

    def symmetry: Int =
      symmetryIndices.size